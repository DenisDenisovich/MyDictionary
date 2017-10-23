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

    void showDictionary(ArrayList<Map<String, Object>> data);
    void setFromKeys(String ... key);
    Map<String,Object> getNewWord();
    ArrayList<Long> getDeletedWords();


}
