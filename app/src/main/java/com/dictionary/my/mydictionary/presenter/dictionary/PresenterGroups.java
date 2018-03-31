package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 27.03.2018.
 */

public interface PresenterGroups<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void update();
    void saveListState();
    void newSelected();
    void newGroupIsReady();
    void deleteSelected();
    void deleteGroupsIsReady();
    void editSelected();
    void editGroupIsReady();
}
