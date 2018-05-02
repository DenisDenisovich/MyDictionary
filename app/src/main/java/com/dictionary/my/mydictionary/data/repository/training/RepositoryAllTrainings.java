package com.dictionary.my.mydictionary.data.repository.training;

import java.util.ArrayList;

import io.reactivex.Single;


/**
 * This class is used for AllTrainings Presenter
 */

public interface RepositoryAllTrainings {
    Integer ENG_RUS_ROWID = 1;
    Integer RUS_ENG_ROWID = 2;
    Integer CONSTRUCTOR_ROWID = 3;
    Integer SPRINT_ROWID = 4;
    Single<Integer> getCountOfEngRusWords();
    Single<Integer> getCountOfRusEngWords();
    Single<Integer> getCountOfConstructorWords();
    Single<Integer> getCountOfSprintWords();
    void destroy();
}
