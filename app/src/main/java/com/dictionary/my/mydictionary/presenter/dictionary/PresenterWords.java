package com.dictionary.my.mydictionary.presenter.dictionary;


public interface PresenterWords<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void saveListState();
    void deleteSelected();
    void deleteWordsIsReady();
    void moveToGroupSelected();
    void moveToGroupWordsIsReady();
    void moveToTrainingSelected();
}
