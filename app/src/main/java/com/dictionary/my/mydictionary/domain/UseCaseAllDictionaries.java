package com.dictionary.my.mydictionary.domain;


import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 30.09.2017.
 */

public interface UseCaseAllDictionaries {
    Observable<Map<String,Object>> getDictionariesList();
    void setNewDictionary(Single<Map<String,Object>> observable);
    void deleteDictionaries(Single<ArrayList<Long>> observable);
    void deleteDictionaryWithWords(Single<Long> observable);
    void editDictionary(Single<Map<String, Object>> observable);
    void destroy();
}
