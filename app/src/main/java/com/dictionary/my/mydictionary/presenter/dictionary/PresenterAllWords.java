package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 10.03.2018.
 */

public interface PresenterAllWords<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void saveListState();
    void newWord();
    void deleteWords();
    void getListOfGroups();
    void moveWords();
    void editWord();
}
