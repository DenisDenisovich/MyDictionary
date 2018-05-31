package com.dictionary.my.mydictionary.presenter.statistic;

/**
 * Created by luxso on 31.05.2018.
 */

public interface PresenterStatistic<V> {
    void attach(V view);
    void detach();
    void destroy();
    void init();
    void setWordsInMonths();
    void setPartOfSpeech();
    void update();
}
