package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryFullInfoWord;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryFullInfoWordImpl;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterFullInfoWord;
import com.dictionary.my.mydictionary.view.dictionary.ViewFullInfoWord;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 30.05.2018.
 */

public class PresenterFullInfoWordImpl<V extends ViewFullInfoWord> implements PresenterFullInfoWord<V> {
    RepositoryFullInfoWord repository;
    DisposableSingleObserver<WordFullInformation> disposable;
    WordFullInformation word;
    V view;
    public PresenterFullInfoWordImpl(Context context){
        repository = new RepositoryFullInfoWordImpl(context);
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
    public void destroy() {
        view = null;
        repository.destroy();
    }

    @Override
    public void init(String id) {
        disposable = repository.getWord(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WordFullInformation>() {
                    @Override
                    public void onSuccess(WordFullInformation wordFullInformation) {
                        view.setWord(wordFullInformation);
                        word = wordFullInformation;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showERROR("ERROR");
                    }
                });
    }

    @Override
    public void update() {
        view.setWord(word);
    }
}
