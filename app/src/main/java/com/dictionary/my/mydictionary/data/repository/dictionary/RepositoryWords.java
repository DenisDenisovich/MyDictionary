package com.dictionary.my.mydictionary.data.repository.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface RepositoryWords {
    Integer ENG_RUS_TRAINING_ROWID = 1;
    Integer RUS_ENG_TRAINING_ROWID = 2;
    Integer CONSTRUCTOR_TRAINING_ROWID = 3;
    Integer SPRINT_TRAINING_ROWID = 4;
    Integer FOR_ALL_TRAININGS_ROWID = 5;
    int MAX_COUNT_OF_WORDS_IN_TRAINIGS = 20;
    Single<ArrayList<Word>> getListOfWords();
    Single<ArrayList<Group>> getListOfGroups();
    Single<ArrayList<Translation>> getTranslation(String word);
    Completable setNewWord(Translation translation);
    Completable setNewWordWithoutInternet(Translation translation);
    Completable deleteWords(ArrayList<String> delList);
    Completable moveWords(ArrayList<String> moveList);

    Single<ArrayList<Long>> getListOfTrainings();
    Completable setWordsToTraining(ArrayList<Long> longs, int rowid);
    void destroy();
}
