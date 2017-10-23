package com.dictionary.my.mydictionary.presenter.dictionary;

import android.content.Context;

import com.dictionary.my.mydictionary.view.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 23.09.2017.
 */

public class DictionaryPresenterImpl<V extends Dictionary> implements DictionaryPresenter<V> {
    Context context;
    V view;
    ArrayList<Map<String,Object>> data;
    Map<String,Object> newWord;
    ArrayList<Long> delWords;
    String[] from = {"word", "translate"};
    String[] word = {"Training","Word","Man","Exception","DictionaryView","Table","Apple","Up","Down","World","Note"};
    String[] translate = {"Тренировка","Слово","Человек","Исключение","Словарь","Стол","Яблоко","Верх","Низ","Мир","Тетрадь"};

    public DictionaryPresenterImpl(Context context){
        this.context = context;
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
        view.setFromKeys(from);
        view.showDictionary(data);
    }


    @Override
    public void newWord() {
        newWord = view.getNewWord();
    }

    @Override
    public void deleteWord() {
        delWords = view.getDeletedWords();
    }
}
