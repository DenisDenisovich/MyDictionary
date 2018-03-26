package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllGroups;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryAllGroupsImpl;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAllGroups;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllGroups;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 27.03.2018.
 */

public class PresenterAllGroupsImpl<V extends ViewAllGroups> implements PresenterAllGroups<V> {
    private final static String LOG_TAG = "Log_PresenterAllGroup";
    private V view;
    private RepositoryAllGroups repository;
    private ArrayList<Group> groups;
    private DisposableCompletableObserver newDisposable;
    private DisposableCompletableObserver delDisposable;
    private DisposableCompletableObserver editDisposable;
    public PresenterAllGroupsImpl(Context context){
        repository = new RepositoryAllGroupsImpl(context);
    }
    @Override
    public void attach(V view) {
        Log.d(LOG_TAG, "attach()");
        this.view = view;
    }

    @Override
    public void detach() {
        Log.d(LOG_TAG, "detach()");
        view = null;
    }

    @Override
    public void destroy() {
        Log.d(LOG_TAG, "destroy()");
        if(newDisposable != null){
            newDisposable.dispose();
        }
        if(delDisposable != null){
            delDisposable.dispose();
        }
        if(editDisposable != null){
            editDisposable.dispose();
        }
        repository.destroy();
    }

    @Override
    public void init() {
        Log.d(LOG_TAG, "init()");
        repository.getListOfGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Group>>() {
                    @Override
                    public void onSuccess(ArrayList<Group> items) {
                        groups = items;
                        view.createList(groups);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Get list of groups ERROR");
                    }
                });
    }

    @Override
    public void update() {
        Log.d(LOG_TAG, "update()");
        view.createList(groups);
    }

    @Override
    public void newSelected() {
        view.createNewGroupDialog();
    }

    @Override
    public void newGroupIsReady() {
        Group group = view.getNewGroup();
        newDisposable = repository.setNewGroup(group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Set new group ERROR");
                    }
                });
    }

    @Override
    public void deleteSelected() {
        view.createDeleteDialog();
    }

    @Override
    public void deleteWordsIsReady() {
        ArrayList<Long> delList = view.getDeletedGroups();
        delDisposable = repository.deleteGroups(delList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        view.deleteGroupFromList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Delete group ERROR");
                    }
                });
    }

    @Override
    public void editSelected() {
        view.createEditDialog();
    }

    @Override
    public void editGroupIsReady() {
        Group group = view.getEditedGroup();
        editDisposable = repository.editGroup(group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        view.editGroupInList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Edit group ERROR");
                    }
                });
    }
}
