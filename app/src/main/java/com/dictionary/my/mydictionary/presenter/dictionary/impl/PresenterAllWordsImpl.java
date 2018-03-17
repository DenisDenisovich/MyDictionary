package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllWords;
import com.dictionary.my.mydictionary.data.repository.dictionary.impl.RepositoryAllWordsImpl;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAllWords;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;

import java.util.ArrayList;

/**
 * Created by luxso on 11.03.2018.
 */

public class PresenterAllWordsImpl<V extends ViewAllWords> implements PresenterAllWords<V> {
    V view;
    RepositoryAllWords repository;
    Long topVisiblePosition;
    ArrayList<Long> deleteWords;
    ArrayList<Word> words;
    public PresenterAllWordsImpl(Context context){
        repository = new RepositoryAllWordsImpl(context);
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

    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void saveListState() {

    }

    @Override
    public void getTranslation() {

    }

    @Override
    public void newWord() {

    }

    @Override
    public void deleteWords() {

    }

    @Override
    public void getListOfGroups() {

    }

    @Override
    public void moveWords() {

    }

    @Override
    public void editWord() {

    }
}
