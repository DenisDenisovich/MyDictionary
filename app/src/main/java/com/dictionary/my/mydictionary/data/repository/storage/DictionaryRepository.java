package com.dictionary.my.mydictionary.data.repository.storage;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 30.10.2017.
 */

public interface DictionaryRepository {
    Observable<Map<String,Object>> getWordList();
    Observable<Map<String,Object>> getDictionaryList();
    void setNewWord(Single<Map<String,Object>> observable);
    void deleteWords(Single<ArrayList<Long>> observable);
    void moveWords(Observable<Long> observable);
    void editWord(Single<Map<String, Object>> observable);
    void destroy();
}