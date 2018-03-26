package com.dictionary.my.mydictionary.presenter.dictionary;


public interface PresenterAllWords<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void deleteSelected();
    void deleteWordsIsReady();
    void moveToGroupSelected();
    void moveToGroupWordsIsReady();
    void moveToTrainingSelected();
}
