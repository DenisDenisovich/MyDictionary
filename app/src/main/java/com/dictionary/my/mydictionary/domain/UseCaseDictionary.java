package com.dictionary.my.mydictionary.domain;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 30.10.2017.
 */

public interface UseCaseDictionary {
    Observable<Map<String,Object>> getWordsList();
    Observable<Map<String,Object>> getDictionaryList();
    void setNewWord(Single<Map<String,Object>> observable);
    void deleteWords(Single<ArrayList<Long>> observable);
    void moveWords(Observable<Long> observable);
    void editWord(Single<Map<String, Object>> observable);
    void destroy();
}
