package com.dictionary.my.mydictionary.view.dictionary;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 27.09.2017.
 */

public interface AllDictionaries {
    void showProgress();
    void hideProgress();
    void showToast(String message);
    void createAdapter(ArrayList<Map<String, Object>> data, String ... key);
    void createDictionariesList();

    Map<String,Object> getNewDictionary();
    ArrayList<Long> getDeletedDictionary();
    Map<String,Object> getEditedDictionary();
}
