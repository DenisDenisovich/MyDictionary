package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 24.09.2017.
 */

public interface DictionaryPresenter<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init(long dictionaryId);
    void update();
    void newWord();
    void deleteWords();
    void moveWords();
    void editWord();
}
