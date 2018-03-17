package com.dictionary.my.mydictionary.data.db.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import java.util.ArrayList;

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
