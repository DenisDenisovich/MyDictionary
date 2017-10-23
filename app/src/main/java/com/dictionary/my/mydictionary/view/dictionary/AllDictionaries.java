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
    void setData(ArrayList<Map<String, Object>> data);
    void createDictionariesList();
    //void updateDictionariesList(ArrayList<Map<String, Object>> data);
    //void setFromKeys(String ... key);

    Map<String,Object> getNewDictionary();
    ArrayList<Long> getDeletingDictionary();
    Map<String,Object> getEditingDictionary();
}
