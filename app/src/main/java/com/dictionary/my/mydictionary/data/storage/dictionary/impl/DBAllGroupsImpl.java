package com.dictionary.my.mydictionary.data.storage.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.storage.dictionary.DBAllGroups;
import com.dictionary.my.mydictionary.data.storage.dictionary.DBAllWords;
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

public class DBAllGroupsImpl implements DBAllGroups {
    DBHelper dbHelper;
    SQLiteDatabase db;
    DisposableSingleObserver<Group> disposableNewDict;
    DisposableSingleObserver<ArrayList<Long>> disposableDelDict;
    DisposableSingleObserver<Group> disposableEditDict;

    public DBAllGroupsImpl(Context context){

        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    @Override
    public Single<ArrayList<Group>> getListOfGroups(){
        /*Observable<Group> observable = Observable.create(new ObservableOnSubscribe<Group>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Group> e) throws Exception {
                Cursor cursor = db.rawQuery(Content.selectDbAllDictionaries, null);
                Map<String,Object> item;
                try {
                    if(cursor.moveToLast()){

                        do{
                            item = CursorToMapAllDictionaries.map(cursor);
                            e.onNext(new Group());
                        }while (cursor.moveToPrevious());
                    }
                    e.onComplete();
                }catch (Throwable t){
                    e.onError(t);
                }
            }
        });*/
        return null;
    }
    @Override
    public void setNewGroup(Single<Group> observable){
        disposableNewDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<Group>() {
            ContentValues cv = new ContentValues();
            @Override
            public void onSuccess(@NonNull Group stringObjectMap) {
                //cv = MapToContentValuesAllDictionaries.map(stringObjectMap);
                //db.insert(Content.TABLE_DICTIONARIES, null, cv);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteGroups(Single<ArrayList<Long>> observable){
        disposableDelDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<ArrayList<Long>>() {
            @Override
            public void onSuccess(@NonNull ArrayList<Long> longs) {
                String[] whereArg = new String[longs.size()];
                for(int i = 0; i < longs.size();i++){
                    whereArg[i] = longs.get(i).toString();
                   //db.delete(Content.TABLE_DICTIONARIES,Content.deleteDbAllDictionaries + whereArg[i], null);
                   // db.delete(Content.TABLE_ALL_WORD,Content.deleteDbAllDictionariesWithWords + whereArg[i], null);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void editGroup(Single<Group> observable) {
        disposableEditDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<Group>() {
            ContentValues cv = new ContentValues();
            Long idOfModifiedDictionary;
            @Override
            public void onSuccess(@NonNull Group stringObjectMap) {
                //idOfModifiedDictionary = (Long) stringObjectMap.get(Content.fromAllDictionaries[0]);
                //cv = MapToContentValuesAllDictionaries.map(stringObjectMap);
                //db.update(Content.TABLE_DICTIONARIES,cv,Content.editDbAllDictionaries + idOfModifiedDictionary,null);
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
