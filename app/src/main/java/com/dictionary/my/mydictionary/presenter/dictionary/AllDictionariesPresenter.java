package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 27.09.2017.
 */

public interface AllDictionariesPresenter<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void newDictionary();
    void deleteDictionary();
    void deleteDictionaryWithWords();
    void editDictionary();
}
