package com.dictionary.my.mydictionary.data.repository.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;


public interface RepositoryAllWords {
    Single<ArrayList<Word>> getListOfWords();
    Single<ArrayList<Group>> getListOfGroups();
    Single<ArrayList<Translation>> getTranslation(String word);
    void setNewWord(Single<Translation> observable);
    void deleteWords(Single<ArrayList<Long>> observable);
    void moveWords(Single<ArrayList<Long>> observable);
    void editWord(Single<Word> observable);
    void destroy();
}
