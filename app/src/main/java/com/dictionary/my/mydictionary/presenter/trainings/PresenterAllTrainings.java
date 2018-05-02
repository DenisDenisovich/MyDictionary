package com.dictionary.my.mydictionary.presenter.trainings;

/**
 * Created by luxso on 31.03.2018.
 */

public interface PresenterAllTrainings<V> {
    void attach(V view);
    void detach();
    void init();
    void update();
    void myWordsSelected();
    void recentlyAddedSelected();
    void engRusClicked();
    void rusEngClicked();
    void constructorClicked();
    void sprintClicked();
    void destroy();
}
