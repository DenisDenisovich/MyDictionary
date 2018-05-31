package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 30.05.2018.
 */

public interface PresenterFullInfoWord<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init(String id);
    void update();
}
