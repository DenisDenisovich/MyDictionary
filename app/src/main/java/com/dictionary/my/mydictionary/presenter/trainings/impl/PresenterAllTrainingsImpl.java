package com.dictionary.my.mydictionary.presenter.trainings.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.repository.training.RepositoryAllTrainings;
import com.dictionary.my.mydictionary.data.repository.training.impl.RepositoryAllTrainingsImpl;
import com.dictionary.my.mydictionary.presenter.trainings.PresenterAllTrainings;
import com.dictionary.my.mydictionary.view.trainings.ViewAllTrainings;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * presenter for AllTrainingsFragment
 */

public class PresenterAllTrainingsImpl<V extends ViewAllTrainings> implements PresenterAllTrainings<V> {
    private final static String LOG_TAG = "Log_PresAllTrainings";
    V view;
    RepositoryAllTrainings repository;
    private Integer spinnerItem = 0;
    private Boolean engRusIsClicked = false;
    private Integer engRusCountOfWords;
    private Boolean rusEngIsClicked = false;
    private Integer rusEngCountOfWords;
    private Boolean constructorIsClicked = false;
    private Integer constructorCountOfWords;
    private Boolean sprintIsClicked = false;
    private Integer sprintCountOfWords;
    private DisposableSingleObserver engRusDisposable;
    private DisposableSingleObserver rusEngDisposable;
    private DisposableSingleObserver constructorDisposable;
    private DisposableSingleObserver sprintDisposable;
    public PresenterAllTrainingsImpl(Context context){
        repository = new RepositoryAllTrainingsImpl(context);
    }
    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void init() {
        Log.d(LOG_TAG, "init() spinnerItem=" + spinnerItem);
        view.setSpinnerItem(spinnerItem);
        if(spinnerItem == 0){
            engRusDisposable = repository.getCountOfEngRusWords()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            engRusCountOfWords = integer;
                            view.setEngRusCountOfWords(engRusCountOfWords);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.setERROR("ERROR of getting Eng Rus count of words");
                        }
                    });
            rusEngDisposable = repository.getCountOfRusEngWords()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            rusEngCountOfWords = integer;
                            view.setRusEngCountOfWords(rusEngCountOfWords);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.setERROR("ERROR of getting Rus Eng count of words");
                        }
                    });
            constructorDisposable = repository.getCountOfConstructorWords()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            constructorCountOfWords = integer;
                            view.setConstructorCountOfWords(constructorCountOfWords);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.setERROR("ERROR of getting Constructor count of words");
                        }
                    });
            sprintDisposable = repository.getCountOfSprintWords()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            sprintCountOfWords = integer;
                            view.setSprintCountOfWords(sprintCountOfWords);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.setERROR("ERROR of getting Sprint count of words");
                        }
                    });
        }else {
            // getting words for recently added words
        }
    }

    @Override
    public void update() {
        Log.d(LOG_TAG, "update()");
        view.setSpinnerItem(spinnerItem);
        view.setEngRusCountOfWords(engRusCountOfWords);
        view.setRusEngCountOfWords(rusEngCountOfWords);
        view.setConstructorCountOfWords(constructorCountOfWords);
        view.setSprintCountOfWords(sprintCountOfWords);
    }

    @Override
    public void myWordsSelected() {
        Log.d(LOG_TAG, "myWordsSelected()");
        spinnerItem = 0;
        init();
    }

    @Override
    public void recentlyAddedSelected() {
        Log.d(LOG_TAG, "recentlyAddedSelected()");
        spinnerItem = 1;
        init();
    }

    @Override
    public void engRusClicked() {
        Log.d(LOG_TAG, "engRusClicked()");
        engRusIsClicked = true;
        rusEngIsClicked = false;
        constructorIsClicked = false;
        sprintIsClicked = false;
    }

    @Override
    public void rusEngClicked() {
        Log.d(LOG_TAG, "rusEngClicked()");
        engRusIsClicked = false;
        rusEngIsClicked = true;
        constructorIsClicked = false;
        sprintIsClicked = false;
    }

    @Override
    public void constructorClicked() {
        Log.d(LOG_TAG, "constructorClicked()");
        engRusIsClicked = false;
        rusEngIsClicked = false;
        constructorIsClicked = true;
        sprintIsClicked = false;
    }

    @Override
    public void sprintClicked() {
        Log.d(LOG_TAG, "sprintClicked()");
        engRusIsClicked = false;
        rusEngIsClicked = false;
        constructorIsClicked = false;
        sprintIsClicked = true;
    }

    @Override
    public void destroy() {
        Log.d(LOG_TAG, "destroy()");
        if(engRusDisposable != null){
            engRusDisposable.dispose();
        }
        if(rusEngDisposable != null){
            rusEngDisposable.dispose();
        }
        if(constructorDisposable != null){
            constructorDisposable.dispose();
        }
        if(sprintDisposable != null){
            sprintDisposable.dispose();
        }
        repository.destroy();
    }
}
