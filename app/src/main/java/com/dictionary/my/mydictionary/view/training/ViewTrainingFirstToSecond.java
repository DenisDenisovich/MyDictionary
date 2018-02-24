package com.dictionary.my.mydictionary.view.training;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 23.02.2018.
 */

public interface ViewTrainingFirstToSecond {
    void setWord(String word);
    void showProgress();
    void hideProgress();
    void setTranslates(ArrayList<Map<String,Object>> translations);
    long getSelectedTranslate();
    void setPositiveAnswer();
    void setNegativeAnswer();
    void setResultMessage(String message);
    void setErrorMessage(String message);
}
