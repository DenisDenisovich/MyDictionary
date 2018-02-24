package com.dictionary.my.mydictionary.data.repository.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.domain.dictionary.mappers.CursorToMapAllDictionaries;
import com.dictionary.my.mydictionary.domain.dictionary.mappers.MapToContentValuesAllDictionaries;

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
 * Created by luxso on 30.09.2017.
 */

public class AllDictionariesRepositoriesImpl implements AllDictionariesRepositories {
    DBHelper dbHelper;
    SQLiteDatabase db;
    DisposableSingleObserver<Map<String,Object>> disposableNewDict;
    DisposableSingleObserver<ArrayList<Long>> disposableDelDict;
    DisposableSingleObserver<Map<String,Object>> disposableEditDict;

    public AllDictionariesRepositoriesImpl(Context context){

        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public Observable<Map<String,Object>> getDictionariesList(){
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
    public void setNewDictionary(Single<Map<String,Object>> observable){
        disposableNewDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<Map<String, Object>>() {
            ContentValues cv = new ContentValues();
            @Override
            public void onSuccess(@NonNull Map<String, Object> stringObjectMap) {
                cv = MapToContentValuesAllDictionaries.map(stringObjectMap);
                db.insert(Content.TABLE_DICTIONARIES, null, cv);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteDictionaries(Single<ArrayList<Long>> observable){
        disposableDelDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<ArrayList<Long>>() {
            @Override
            public void onSuccess(@NonNull ArrayList<Long> longs) {
                String[] whereArg = new String[longs.size()];
                for(int i = 0; i < longs.size();i++){
                    whereArg[i] = longs.get(i).toString();
                    db.delete(Content.TABLE_DICTIONARIES,Content.deleteDbAllDictionaries + whereArg[i], null);
                    db.delete(Content.TABLE_ALL_WORD,Content.deleteDbAllDictionariesWithWords + whereArg[i], null);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void editDictionary(Single<Map<String, Object>> observable) {
        disposableEditDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<Map<String, Object>>() {
            ContentValues cv = new ContentValues();
            Long idOfModifiedDictionary;
            @Override
            public void onSuccess(@NonNull Map<String, Object> stringObjectMap) {
                idOfModifiedDictionary = (Long) stringObjectMap.get(Content.fromAllDictionaries[0]);
                cv = MapToContentValuesAllDictionaries.map(stringObjectMap);
                db.update(Content.TABLE_DICTIONARIES,cv,Content.editDbAllDictionaries + idOfModifiedDictionary,null);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void destroy() {
        if(disposableNewDict != null) {
            disposableNewDict.dispose();
        }
        if(disposableDelDict != null) {
            disposableDelDict.dispose();
        }
        if(disposableEditDict != null){
            disposableEditDict.dispose();
        }
        db.close();
    }

}
