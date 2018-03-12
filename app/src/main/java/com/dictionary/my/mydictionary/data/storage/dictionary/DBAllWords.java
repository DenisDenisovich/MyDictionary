package com.dictionary.my.mydictionary.data.storage.dictionary;

import com.dictionary.my.mydictionary.data.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.entites.dictionary.WordFullInformation;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 30.09.2017.
 */

public interface DBAllWords {
    Single<ArrayList<Word>> getListOfWord();
    void setNewWord(Single<WordFullInformation> observable);
    void deleteWords(Single<ArrayList<Long>> observable);
    void moveWords(Single<ArrayList<Long>> observable);
    void editWord(Single<Word> observable);
    void destroy();
}
