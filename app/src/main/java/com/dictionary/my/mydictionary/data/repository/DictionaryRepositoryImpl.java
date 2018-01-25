package com.dictionary.my.mydictionary.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.domain.mappers.CursorToContentValuesDictionary;
import com.dictionary.my.mydictionary.domain.mappers.CursorToMapAllDictionaries;
import com.dictionary.my.mydictionary.domain.mappers.CursorToMapDictionary;
import com.dictionary.my.mydictionary.domain.mappers.MapToContentValuesDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 23.09.2017.
 */

public class DictionaryRepositoryImpl implements DictionaryRepository {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Long currentDictionaryId;
    Long moveToDictionaryId;
    DisposableSingleObserver<Map<String,Object>> disposableNewWords;
    DisposableSingleObserver<ArrayList<Long>> disposableDelWords;
    DisposableObserver<Long> disposableMoveWords;
    DisposableSingleObserver<Long> disposableMoveToDictionaryId;
    DisposableSingleObserver<Map<String,Object>> disposableEditWord;

    public DictionaryRepositoryImpl(Context context, Long currentDictionaryId){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        this.currentDictionaryId = currentDictionaryId;
    }


    @Override
    public Observable<Map<String, Object>> getWordList() {
        Observable<Map<String,Object>> observable = Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Map<String, Object>> e) throws Exception {
                Cursor cursor = db.rawQuery(Content.selectDbDictionary, new String[] {currentDictionaryId.toString()});
                Map<String,Object> item;
                try {
                    if(cursor.moveToLast()){

                        do{
                            item = CursorToMapDictionary.map(cursor);
                            e.onNext(item);
                        }while (cursor.moveToPrevious());
                    }
                    e.onComplete();
                }catch (Throwable t){
                    e.onError(t);
                }
            }
        });
        return observable;
    }

    @Override
    public Observable<Map<String, Object>> getDictionaryList() {
        Observable<Map<String,Object>> observable = Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Map<String, Object>> e) throws Exception {
                Cursor cursor = db.rawQuery(Content.selectDbAllDictionaries, null);
                Map<String,Object> item;
                try {
                    if(cursor.moveToLast()){

                        do{
                            item = CursorToMapAllDictionaries.map(cursor);
                            e.onNext(item);
                        }while (cursor.moveToPrevious());
                    }
                    e.onComplete();
                }catch (Throwable t){
                    e.onError(t);
                }
            }
        });
        return observable;
    }

    @Override
    public void setNewWord(Single<Map<String, Object>> observable) {
        disposableNewWords = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Map<String, Object>>() {
            ContentValues cv = new ContentValues();
            @Override
            public void onSuccess(@NonNull Map<String, Object> stringObjectMap) {
                cv = MapToContentValuesDictionary.map(stringObjectMap);
                cv.put(Content.COLUMN_DICTIONARY,currentDictionaryId);
                db.insert(Content.TABLE_ALL_WORD,null,cv);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void deleteWords(Single<ArrayList<Long>> observable) {
        disposableDelWords = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Long>>() {
                    @Override
                    public void onSuccess(@NonNull ArrayList<Long> longs) {
                        String[] whereArg = new String[longs.size()];
                        for(int i = 0; i < longs.size();i++){
                            whereArg[i] = longs.get(i).toString();
                            db.delete(Content.TABLE_ALL_WORD,Content.deleteDbDictionary + whereArg[i], null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void moveWords(Observable<Long> observable) {
        disposableMoveWords = observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Long>() {
                    ArrayList<Long> movedItems = new ArrayList<>();
                    @Override
                    public void onNext(@NonNull Long aLong) {

                        Log.d("LOG_TAG", "добавил - " + aLong);
                        movedItems.add(aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("LOG_TAG", "зашел во второй onError");
                        //e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {
                        String strPlacehold = "(";
                        String[] strArg = new String[movedItems.size()-1];
                        ContentValues cv;
                        db.beginTransaction();
                        moveToDictionaryId = movedItems.get(0);
                        for(int i = 1; i < movedItems.size()-1; i++){
                            strPlacehold = strPlacehold.concat("?,");
                            strArg[i-1] = movedItems.get(i).toString();
                            
                        }
                        strPlacehold = strPlacehold.concat("?)");
                        strArg[movedItems.size()-2] = movedItems.get(movedItems.size()-1).toString();



                        /*String[] from = {Content.COLUMN_ROWID, Content.COLUMN_WORD,
                                Content.COLUMN_TRANSLATE, Content.COLUMN_DICTIONARY};
                        ///////////////////////BEGIN/////////////////
                        Cursor c = db.query(Content.TABLE_ALL_WORD,from,null,null,null,null,null);
                        if(c.moveToFirst()){
                            do{
                                Log.d("LOG_TAG",Content.COLUMN_ROWID + ": " + c.getString(c.getColumnIndex(Content.COLUMN_ROWID)) +
                                          ", " +Content.COLUMN_WORD + ": " + c.getString(c.getColumnIndex(Content.COLUMN_WORD)) +
                                          ", " +Content.COLUMN_TRANSLATE + ": " + c.getString(c.getColumnIndex(Content.COLUMN_TRANSLATE)) +
                                          ", " +Content.COLUMN_DICTIONARY + ": " + c.getString(c.getColumnIndex(Content.COLUMN_DICTIONARY))
                                );
                            }while (c.moveToNext());
                        }
                        /////////////////////////END///////////////*/

                        Cursor cursor = db.rawQuery( "SELECT " + Content.COLUMN_ROWID + ", " + Content.COLUMN_WORD + ", " + Content.COLUMN_TRANSLATE + ", " + Content.COLUMN_DICTIONARY +
                                " FROM " + Content.TABLE_ALL_WORD + " WHERE " + Content.COLUMN_ROWID + " IN " + strPlacehold,strArg);
                        Log.d("LOG_TAG", "SELECT * FROM " + Content.TABLE_ALL_WORD + " WHERE " + Content.COLUMN_ROWID + " IN " + strPlacehold +
                                ", " + Arrays.toString(strArg));

                        if(cursor.moveToFirst()){
                            do{
                                cv = CursorToContentValuesDictionary.map(cursor,moveToDictionaryId);
                                db.update(Content.TABLE_ALL_WORD,cv, Content.COLUMN_ROWID + " = ?", new String[] {(String)cv.get(Content.COLUMN_ROWID)});
                            }while (cursor.moveToNext());
                        }

                        /*///////////////////////BEGIN///////////////
                        c = db.query(Content.TABLE_ALL_WORD,from,null,null,null,null,null);
                        if(c.moveToFirst()){
                            do{
                                Log.d("LOG_TAG",Content.COLUMN_ROWID + ": " + c.getString(c.getColumnIndex(Content.COLUMN_ROWID)) +
                                        ", " +Content.COLUMN_WORD + ": " + c.getString(c.getColumnIndex(Content.COLUMN_WORD)) +
                                        ", " +Content.COLUMN_TRANSLATE + ": " + c.getString(c.getColumnIndex(Content.COLUMN_TRANSLATE)) +
                                        ", " +Content.COLUMN_DICTIONARY + ": " + c.getString(c.getColumnIndex(Content.COLUMN_DICTIONARY))
                                );
                            }while (c.moveToNext());
                        } else{
                            Log.d("LOG_TAG", "c пустой");
                        }
                        /////////////////////////END////////////////////*/

                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                });

    }

    @Override
    public void editWord(Single<Map<String, Object>> observable) {
        disposableEditWord = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Map<String, Object>>() {
                    ContentValues cv = new ContentValues();
                    Long idOfModifiedWord;
                    @Override
                    public void onSuccess(@NonNull Map<String, Object> stringObjectMap) {
                        idOfModifiedWord = (Long)stringObjectMap.get(Content.fromDictionary[0]);
                        cv = MapToContentValuesDictionary.map(stringObjectMap);
                        db.update(Content.TABLE_ALL_WORD,cv,Content.editDbDictionary + idOfModifiedWord,null);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                       e.printStackTrace();
                    }
                });
    }

    @Override
    public void destroy() {
        if(disposableNewWords != null) {
            disposableNewWords.dispose();
        }
        if(disposableDelWords != null) {
            disposableDelWords.dispose();
        }
        if(disposableEditWord != null){
            disposableEditWord.dispose();
        }
        if(disposableMoveWords != null){
            disposableMoveWords.dispose();
        }
        if(disposableMoveToDictionaryId != null){
            disposableMoveToDictionaryId.dispose();
        }
        db.close();
    }
}
