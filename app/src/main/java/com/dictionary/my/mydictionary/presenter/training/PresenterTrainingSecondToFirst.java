package com.dictionary.my.mydictionary.presenter.training;

/**
 * Created by luxso on 01.02.2018.
 */

public interface PresenterTrainingSecondToFirst<V> {
    void attach(V v);
    void detach();
    void destroy();
    void init();
    void update();
    void TranslateIsSelected();
}
