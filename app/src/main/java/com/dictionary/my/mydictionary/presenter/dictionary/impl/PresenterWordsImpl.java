package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryWords;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryWordsImpl;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterWords;
import com.dictionary.my.mydictionary.view.dictionary.AllWordsFragment;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 11.03.2018.
 */

public class PresenterWordsImpl<V extends AllWordsFragment> implements PresenterWords<V> {
    private final static String LOG_TAG = "Log_PresenterAllWord";
    private V view;
    private RepositoryWords repository;
    private ArrayList<Word> words;
    private DisposableCompletableObserver delDisposable;
    private DisposableCompletableObserver moveDisposable;

    public PresenterWordsImpl(Context context){
        repository = new RepositoryWordsImpl(context);
    }

    public PresenterWordsImpl(Context context, Long groupId){
        repository = new RepositoryWordsImpl(context, groupId);
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
        if(delDisposable != null){
            delDisposable.dispose();
        }
        if(moveDisposable != null){
            moveDisposable.dispose();
        }
        repository.destroy();
    }

    @Override
    public void init() {
        Log.d(LOG_TAG, "init()");
        repository.getListOfWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Word>>() {
                    @Override
                    public void onSuccess(ArrayList<Word> items) {
                        words = items;
                        view.createList(words);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showERROR("Get list of words ERROR");
                    }
                });
    }

    @Override
    public void update() {
        Log.d(LOG_TAG, "update()");
        view.createList(words);
    }


    @Override
    public void deleteSelected() {
        view.createDeleteDialog();
    }

    @Override
    public void deleteWordsIsReady() {
        ArrayList<Long> deleteWords = view.getDeletedWords();
        delDisposable = repository.deleteWords(deleteWords)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        view.deleteWordsFromList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showERROR("Delete word ERROR");
                    }
                });
    }

    @Override
    public void moveToGroupSelected() {
        repository.getListOfGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Group>>() {
                    @Override
                    public void onSuccess(ArrayList<Group> groups) {
                        view.createMoveToGroupDialog(groups);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showERROR("Group ERROR");
                    }
                });

    }

    @Override
    public void moveToTrainingSelected() {

    }

    @Override
    public void moveToGroupWordsIsReady() {
        ArrayList<Long> moveWords = view.getMovedToGroupWords();
        moveDisposable = repository.moveWords(moveWords)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showERROR("Move ERROR");
                    }
                });
    }


}
