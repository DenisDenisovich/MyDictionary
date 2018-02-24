package com.dictionary.my.mydictionary.presenter.training;

/**
 * Created by luxso on 23.02.2018.
 */

public interface PresenterTrainingFirstToSecond<V> {
    void attach(V v);
    void detach();
    void destroy();
    void init();
    void update();
    void TranslateIsSelected();
}
