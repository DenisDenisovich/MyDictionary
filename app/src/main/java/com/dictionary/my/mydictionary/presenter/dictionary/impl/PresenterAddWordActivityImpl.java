package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllWords;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryAllWordsImpl;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAddWordActivity;
import com.dictionary.my.mydictionary.view.dictionary.AddWordActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 19.03.2018.
 */

public class PresenterAddWordActivityImpl<V extends AddWordActivity> implements PresenterAddWordActivity<V> {
    private final static String LOG_TAG = "Log_PresenterAddWord";
    private V view;
    private RepositoryAllWords repository;
    private ArrayList<Translation> words;
    private ArrayList<Group> groups;
    private Translation newWord;
    boolean alternativeTranslationMode = false;
    boolean defaultTranslationMode = true;
    public PresenterAddWordActivityImpl(Context context){
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
    public void update() {
        Log.d(LOG_TAG, "update()");
        if(defaultTranslationMode){
            view.showDefaultTranslationMode();
            view.createListOfTranslation(words);
        } else if(alternativeTranslationMode){
            view.showAlternativeTranslationMode();
        }
        view.hideProgress();
        view.setGroups(groups);
    }

    @Override
    public void initGroupList() {
        repository.getListOfGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Group>>() {
                    @Override
                    public void onSuccess(ArrayList<Group> data) {
                        groups = data;
                        view.setGroups(groups);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("GROUP_ERROR");
                        Log.d(LOG_TAG, e.getMessage());
                    }
                });
    }

    @Override
    public void wordHasPrinted() {
        Log.d(LOG_TAG, "wordHasPrinted()");
        view.showProgress();
        String word = view.getPrintedWord();
        repository.getTranslation(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Translation>>() {
                    @Override
                    public void onSuccess(ArrayList<Translation> translations) {
                        words = translations;
                        view.createListOfTranslation(words);
                        view.hideProgress();
                        view.showDefaultTranslationMode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("TRANSLATION_ERROR");
                        view.hideProgress();
                        view.showAlternativeTranslationMode();
                    }
                });
    }

    @Override
    public void alternativeTranslationModeHasSelected() {
        Log.d(LOG_TAG, "alternativeTranslationModeHasSelected()");
        view.hideProgress();
        view.hideDefaultTranslationMode();
        view.showAlternativeTranslationMode();
    }

    @Override
    public void defaultTranslationModeHasSelected() {
        Log.d(LOG_TAG, "defaultTranslationModeHasSelected()");
        if(words != null){
            view.showDefaultTranslationMode();
        }
        view.hideAlternativeTranslationMode();
    }

    @Override
    public void translationIsReady() {
        Log.d(LOG_TAG, "translationIsReady()");
        newWord = view.getNewTranslation();
        repository.setNewWord(newWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        view.closeActivity();
                        Log.d(LOG_TAG, "in translationIsReady onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Set word ERROR");
                        Log.d(LOG_TAG, "in translationIsReady onError");
                    }
                });
    }

    @Override
    public void translationIsReadyWithoutInternet() {
        Log.d(LOG_TAG, "translationIsReady()");
        newWord = view.getNewTranslationWithoutInternet();
        repository.setNewWordWithoutInternet(newWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        view.closeActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Set word ERROR");
                    }
                });
    }
}
