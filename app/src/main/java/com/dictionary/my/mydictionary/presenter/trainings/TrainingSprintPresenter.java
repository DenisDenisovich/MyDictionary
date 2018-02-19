package com.dictionary.my.mydictionary.presenter.trainings;

/**
 * Created by luxso on 01.02.2018.
 */

public interface TrainingSprintPresenter<V> {
    void attach(V v);
    void detach();
    void destroy();
    void init();
    void update();
    void rightButtonClick();
    void wrongButtonClick();
}
