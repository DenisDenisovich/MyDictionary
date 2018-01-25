package com.dictionary.my.mydictionary.view.dictionary;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 24.09.2017.
 */

public interface Dictionary {
    void showProgress();
    void hideProgress();
    void showToast(String message);
    void createAdapter(ArrayList<Map<String, Object>> data);
    void createWordsList();
    void setFrom(String ... from);
    void setDictionaryList(ArrayList<Map<String,Object>> dictionaryData);
    Map<String,Object> getNewWord();
    ArrayList<Long> getDeletedWords();
    ArrayList<Long> getMovedWords();
    Map<String,Object> getEditedWord();


}
