package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.AndroidException;
import android.util.Log;

import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllWords;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryAllWordsImpl;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAllWords;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 11.03.2018.
 */

public class PresenterAllWordsImpl<V extends ViewAllWords> implements PresenterAllWords<V> {
    private final static String LOG_TAG = "Log_PresenterAllWord";
    V view;
    RepositoryAllWords repository;
    Integer topVisiblePosition;
    ArrayList<Long> deleteWords;
    ArrayList<Word> words;

    public PresenterAllWordsImpl(Context context){
        repository = new RepositoryAllWordsImpl(context);
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
                        view.showERROR("ERROR");
                    }
                });
    }

    @Override
    public void update() {
        Log.d(LOG_TAG, "update()");
        view.createList(words);
        view.setTopVisiblePosition(topVisiblePosition);
    }

    @Override
    public void saveListState() {
        Log.d(LOG_TAG, "saveListState()");
        topVisiblePosition = view.getTopVisiblePosition();
    }


    @Override
    public void deleteWords() {
        Log.d(LOG_TAG, "deleteWords()");
    }

    @Override
    public void getListOfGroups() {
        Log.d(LOG_TAG, "getListOfGroups()");
    }

    @Override
    public void moveWords() {
        Log.d(LOG_TAG, "moveWords()");
    }

    @Override
    public void editWord() {
        Log.d(LOG_TAG, "editWord()");
    }
}
