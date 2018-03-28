package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryWords;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryWordsImpl;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAddWord;
import com.dictionary.my.mydictionary.view.dictionary.AddWordActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 19.03.2018.
 */

public class PresenterAddWordImpl<V extends AddWordActivity> implements PresenterAddWord<V> {
    private final static String LOG_TAG = "Log_PresenterAddWord";
    private V view;
    private RepositoryWords repository;
    private ArrayList<Translation> words;
    private DisposableSingleObserver<ArrayList<Translation>> wordDisposable;
    private ArrayList<Group> groups;
    private Translation newWord;
    private boolean alternativeTranslationMode = false;
    private boolean defaultTranslationModeERROR = false;
    private boolean searchForTranslation = false;
    public PresenterAddWordImpl(Context context){
        repository = new RepositoryWordsImpl(context);
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
        if(wordDisposable != null){
            wordDisposable.dispose();
        }
        repository.destroy();
    }


    @Override
    public void update() {
        Log.d(LOG_TAG, "update()");
        view.hideProgress();
        view.setGroups(groups);
        if(words != null) {
            view.createListOfTranslation(words);
        }
        if(!alternativeTranslationMode){
            if(searchForTranslation){
                view.showProgress();
            }else{
                view.hideProgress();
                if(words != null) {
                    view.showDefaultTranslationMode();
                }
            }
        }else{
            view.hideProgress();
            view.showAlternativeTranslationMode();
        }
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
        view.hideDefaultTranslationMode();
        String word = view.getPrintedWord();
        searchForTranslation = true;
        if(wordDisposable != null){
            wordDisposable.dispose();
        }
        wordDisposable = repository.getTranslation(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Translation>>() {
                    @Override
                    public void onSuccess(ArrayList<Translation> translations) {
                        words = translations;
                        defaultTranslationModeERROR = false;
                        searchForTranslation = false;
                        view.createListOfTranslation(words);
                        view.hideProgress();
                        if(!alternativeTranslationMode) {
                            view.showDefaultTranslationMode();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("TRANSLATION_ERROR");
                        defaultTranslationModeERROR = true;
                        searchForTranslation = false;
                        view.hideProgress();
                        view.hideDefaultTranslationMode();
                        view.showAlternativeTranslationMode();
                    }
                });
    }

    @Override
    public void alternativeTranslationModeHasSelected() {
        Log.d(LOG_TAG, "alternativeTranslationModeHasSelected()");
        alternativeTranslationMode = true;
        view.hideProgress();
        view.hideDefaultTranslationMode();
        view.showAlternativeTranslationMode();
    }

    @Override
    public void defaultTranslationModeHasSelected() {
        Log.d(LOG_TAG, "defaultTranslationModeHasSelected()");
        alternativeTranslationMode = false;
        view.hideAlternativeTranslationMode();
        if(words != null && !defaultTranslationModeERROR && !searchForTranslation){
            view.showDefaultTranslationMode();
        }
        if(searchForTranslation){
            view.showProgress();
        }
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showERROR("Set word ERROR");
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
