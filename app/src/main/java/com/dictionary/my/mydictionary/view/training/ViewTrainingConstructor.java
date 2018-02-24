package com.dictionary.my.mydictionary.view.training;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 01.02.2018.
 */

public interface ViewTrainingConstructor {
    void setWord(String word);
    void setSymbols(ArrayList<String> translate);
    void setCurrentTranslate(String currentTranslate);
    String getClickSymbol();
    void showProgress();
    void hideProgress();
    void setPositiveMessage();
    void setNegativeMessage();
    void setResultMessage(String message);
    void setErrorMessage(String message);
}
