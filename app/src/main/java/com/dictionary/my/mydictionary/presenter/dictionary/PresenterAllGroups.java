package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 27.03.2018.
 */

public interface PresenterAllGroups<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void newSelected();
    void newGroupIsReady();
    void deleteSelected();
    void deleteWordsIsReady();
    void editSelected();
    void editGroupIsReady();
}
