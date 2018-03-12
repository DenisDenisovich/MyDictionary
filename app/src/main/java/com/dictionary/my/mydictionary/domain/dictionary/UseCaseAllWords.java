package com.dictionary.my.mydictionary.domain.dictionary;

import com.dictionary.my.mydictionary.data.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.data.entites.dictionary.Word;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;


public interface UseCaseAllWords {
    Single<ArrayList<Word>> getListOfWords();
    Single<ArrayList<Group>> getListOfGroups();
    Observable<Translation> getTranslation();
    void setNewWord(Single<WordFullInformation> observable);
    void deleteWords(Single<ArrayList<Long>> observable);
    void moveWords(Single<ArrayList<Long>> observable);
    void editWord(Single<Word> observable);
    void destroy();
}
