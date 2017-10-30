package com.dictionary.my.mydictionary.presenter.dictionary;

import android.content.Context;

import com.dictionary.my.mydictionary.domain.UseCaseDictionary;
import com.dictionary.my.mydictionary.domain.UseCaseDictionaryImpl;
import com.dictionary.my.mydictionary.view.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 23.09.2017.
 */

public class DictionaryPresenterImpl<V extends Dictionary> implements DictionaryPresenter<V> {
    V view;
    UseCaseDictionary useCase;
    ArrayList<Map<String,Object>> data;
    Map<String,Object> newWord;
    ArrayList<Long> delWords;


    public DictionaryPresenterImpl(Context context){
        useCase = new UseCaseDictionaryImpl(context);
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
    public void init(long dictionaryId) {

    }

    @Override
    public void update() {

    }


    @Override
    public void newWord() {
        newWord = view.getNewWord();
    }

    @Override
    public void deleteWords() {
        delWords = view.getDeletedWords();
    }

    @Override
    public void moveWords() {

    }

    @Override
    public void editWord() {

    }
}
