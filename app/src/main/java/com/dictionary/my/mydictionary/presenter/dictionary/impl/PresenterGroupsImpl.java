package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryGroups;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryGroupsImpl;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterGroups;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllGroups;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 27.03.2018.
 */

public class PresenterGroupsImpl<V extends ViewAllGroups> implements PresenterGroups<V> {
    private final static String LOG_TAG = "Log_PresenterAllGroup";
    private V view;
    private RepositoryGroups repository;
    private ArrayList<Group> groups;
    private ArrayList<String> selectedItemIds;
    private Boolean selectMode;
    private DisposableCompletableObserver newDisposable;
    private DisposableCompletableObserver delDisposable;
    private DisposableCompletableObserver editDisposable;
    public PresenterGroupsImpl(Context context){
        repository = new RepositoryGroupsImpl(context);
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
                        e.printStackTrace();
                        view.showERROR("Get list of groups ERROR");
                    }
                });
    }

    @Override
    public void update() {
        Log.d(LOG_TAG, "updateView()");
        view.createList(groups);
        view.setSelectedItemIds(selectedItemIds);
        view.setSelectMode(selectMode);
    }

    @Override
    public void saveListState() {
        selectedItemIds = view.getSelectedItemIds();
        selectMode = view.getSelectMode();
    }

    @Override
    public void newSelected() {
        Log.d(LOG_TAG, "newSelected()");
        view.createNewGroupDialog();
    }

    @Override
    public void newGroupIsReady() {
        Log.d(LOG_TAG, "newGroupIsReady()");
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
                        e.printStackTrace();
                        view.showERROR("Set new group ERROR");
                    }
                });
    }

    @Override
    public void deleteSelected() {
        Log.d(LOG_TAG, "deleteSelected()");
        view.createDeleteDialog();
    }

    @Override
    public void deleteGroupsIsReady() {
        Log.d(LOG_TAG, "deleteGroupsIsReady()");
        ArrayList<String> delList = view.getDeletedGroups();
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
                        e.printStackTrace();
                        view.showERROR("Delete group ERROR");
                    }
                });
    }

    @Override
    public void editSelected() {
        Log.d(LOG_TAG, "editSelected()");
        view.createEditDialog();
    }

    @Override
    public void editGroupIsReady() {
        Log.d(LOG_TAG, "editGroupIsReady()");
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
                        e.printStackTrace();
                        view.showERROR("Edit group ERROR");
                    }
                });
    }
}
