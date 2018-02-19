package com.dictionary.my.mydictionary.view.training;

/**
 * Created by luxso on 01.02.2018.
 */

public interface TrainingSprint {
    void showProgress();
    void hideProgress();
    void setWord(String word);
    void setTranslate(String translate);
    void setTime(String time);
    void setPositiveMessage();
    void setNegativeMessage();
    void setResultMessage(String message);
    void setErrorMessage(String message);
}
