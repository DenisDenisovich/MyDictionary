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
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * This class working only with Groups table of dataBase
 */

public class DBAllGroupsImpl implements DBAllGroups {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private DisposableSingleObserver<Group> disposableNewDict;
    private DisposableSingleObserver<ArrayList<Long>> disposableDelDict;
    private DisposableSingleObserver<Group> disposableEditDict;

    public DBAllGroupsImpl(Context context){

        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    @Override
    public Single<ArrayList<Group>> getListOfGroups(){
        return Single.create(new SingleOnSubscribe<ArrayList<Group>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Group>> e) throws Exception {
                try{
                    Cursor cursor = db.query(Content.TABLE_GROUPS,null,null,null,null,null,null);
                    if(cursor.moveToFirst()){
                        ArrayList<Group> list = new ArrayList<>();
                        do{
                            Group item = new Group();
                            item.setId(cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID)));
                            item.setTitle(cursor.getString(cursor.getColumnIndex(Content.COLUMN_TITLE)));
                            list.add(item);
                        }while (cursor.moveToNext());
                        if(!e.isDisposed()){
                            e.onSuccess(list);
                        }
                    }
                }catch (Throwable t){
                    if(!e.isDisposed()){
                        e.onError(t);
                    }
                }
            }
        });
    }
    @Override
    public void setNewGroup(Single<Group> observable){
        disposableNewDict = observable.subscribeOn(Schedulers.io()).subscribeWith(new DisposableSingleObserver<Group>() {
            ContentValues cv = new ContentValues();
            @Override
            public void onSuccess(@NonNull Group group) {
                cv.put(Content.COLUMN_TITLE, group.getTitle());
                db.insert(Content.TABLE_GROUPS, null, cv);
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
                String strPlaceholder = "(";
                String[] whereArg = new String[longs.size()];
                for(int i = 0; i < longs.size()-1;i++){
                    strPlaceholder = strPlaceholder.concat("?,");
                    whereArg[i] = longs.get(i).toString();
                }
                strPlaceholder = strPlaceholder.concat("?)");
                whereArg[whereArg.length-1] = longs.get(longs.size()-1).toString();
                db.delete(Content.TABLE_GROUPS,Content.COLUMN_ROWID + " in " + strPlaceholder, whereArg);
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
            String idOfModifiedGroup;
            @Override
            public void onSuccess(@NonNull Group group) {
                cv.put(Content.COLUMN_TITLE, group.getTitle());
                idOfModifiedGroup = String.valueOf(group.getId());
                db.update(Content.TABLE_GROUPS,cv,Content.COLUMN_ROWID  + " = ?", new String[] {idOfModifiedGroup});
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
