package com.dictionary.my.mydictionary.data.repository.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface RepositoryWords {
    Single<ArrayList<Word>> getListOfWords();
    Single<ArrayList<Group>> getListOfGroups();
    Single<ArrayList<Translation>> getTranslation(String word);
    Completable setNewWord(Translation translation);
    Completable setNewWordWithoutInternet(Translation translation);
    Completable deleteWords(ArrayList<Long> delList);
    Completable moveWords(ArrayList<Long> moveList);

    void destroy();
}
