package com.dictionary.my.mydictionary.view.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

/**
 * Created by luxso on 30.05.2018.
 */

public interface ViewFullInfoWord {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void setWord(WordFullInformation word);
}
