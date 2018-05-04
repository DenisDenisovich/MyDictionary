package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryGroups;
import com.dictionary.my.mydictionary.data.db.dictionary.DBGroups;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBGroupsImpl;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * This class manages all necessary data for AllGroups
 */

public class RepositoryGroupsImpl implements RepositoryGroups {
    private final static String LOG_TAG = "Log_RepositoryAllGroups";
    private DBGroups dbGroups;
    public RepositoryGroupsImpl(Context context){
        dbGroups = new DBGroupsImpl(context);
    }
    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        return Single.create(new SingleOnSubscribe<ArrayList<Group>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Group>> e) throws Exception {
                try{
                    ArrayList<Group> groups = dbGroups.getListOfGroups();
                    if(!e.isDisposed()){
                        e.onSuccess(groups);
                    }
                }catch (SQLiteException | IllegalStateException exc){
                    Log.d(LOG_TAG, "Exception in getListOfGroups()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable setNewGroup(final Group group) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try{
                    dbGroups.setNewGroup(group);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SQLException exc){
                    Log.d(LOG_TAG, "Exception of db.setNewGroup()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable deleteGroups(final ArrayList<String> delList) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    dbGroups.deleteGroups(delList);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SQLiteException | NullPointerException | IndexOutOfBoundsException exc){
                    Log.d(LOG_TAG, "Exception of db.deleteGroups()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable editGroup(final Group editGroup) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    dbGroups.editGroup(editGroup);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SQLiteException | NullPointerException exc){
                    Log.d(LOG_TAG, "Exception of db.editGroup()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public void destroy() {
        dbGroups.destroy();
    }
}
