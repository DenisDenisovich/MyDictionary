package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllGroups;
import com.dictionary.my.mydictionary.data.db.dictionary.DBAllGroups;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBAllGroupsImpl;

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

public class RepositoryAllGroupsImpl implements RepositoryAllGroups {
    private DBAllGroups dbAllGroups;
    public RepositoryAllGroupsImpl(Context context){
        dbAllGroups = new DBAllGroupsImpl(context);
    }
    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        return Single.create(new SingleOnSubscribe<ArrayList<Group>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Group>> e) throws Exception {
                try{
                    ArrayList<Group> groups = dbAllGroups.getListOfGroups();
                    if(!e.isDisposed()){
                        e.onSuccess(groups);
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()){
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
                    dbAllGroups.setNewGroup(group);
                }catch (DBException exc){
                    if(!e.isDisposed()){
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable deleteGroups(final ArrayList<Long> delList) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    dbAllGroups.deleteGroups(delList);
                }catch (DBException exc){
                    if(!e.isDisposed()){
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
                    dbAllGroups.editGroup(editGroup);
                }catch (DBException exc){
                    if(!e.isDisposed()){
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public void destroy() {
        dbAllGroups.destroy();
    }
}
