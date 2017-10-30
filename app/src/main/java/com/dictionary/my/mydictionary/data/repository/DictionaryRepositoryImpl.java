package com.dictionary.my.mydictionary.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.domain.mappers.CursorToMapDictionary;
import com.dictionary.my.mydictionary.domain.mappers.MapToContentValuesDictionary;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 23.09.2017.
 */

public class DictionaryRepositoryImpl implements DictionaryRepository {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Long currentDictionaryId;
    DisposableSingleObserver<Map<String,Object>> disposableNewWords;
    DisposableSingleObserver<ArrayList<Long>> disposableDelWords;
    DisposableSingleObserver<ArrayList<Long>> disposableMoveWords;
    Single<ArrayList<Long>> singleMoveToDictionaryIdWords;
    DisposableSingleObserver<Map<String,Object>> disposableEditWord;

    public DictionaryRepositoryImpl(Context context, Long currentDictionaryId){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        this.currentDictionaryId = currentDictionaryId;
    }


    @Override
    public Observable<Map<String, Object>> getDictionariesList() {
        Observable<Map<String,Object>> observable = Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Map<String, Object>> e) throws Exception {
                Cursor cursor = db.rawQuery(Content.selectDbAllDictionaries, new String[] {currentDictionaryId.toString()});
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
        return null;
    }

    @Override
    public void setNewWord(Single<Map<String, Object>> observable) {
        disposableNewWords = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<Map<String, Object>>() {
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

    }

    @Override
    public void moveWords(Observable<Long> observable) {

    }

    @Override
    public void editWord(Single<Map<String, Object>> observable) {

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
        db.close();
    }
}
