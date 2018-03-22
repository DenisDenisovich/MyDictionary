package com.dictionary.my.mydictionary.presenter.dictionary;


public interface PresenterAllWords<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void saveListState();
    void deleteWords();
    void getListOfGroups();
    void moveWords();
    void editWord();
}
