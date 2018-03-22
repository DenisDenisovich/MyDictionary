package com.dictionary.my.mydictionary.presenter.dictionary;

/**
 * Created by luxso on 19.03.2018.
 */

public interface PresenterAddWordActivity<V> {
    void attach(V view);
    void detach();
    void destroy();
    void update();
    void wordHasPrinted();
    void alternativeTranslationModeHasSelected();
    void defaultTranslationModeHasSelected();
    void translationHasSelected();
}
