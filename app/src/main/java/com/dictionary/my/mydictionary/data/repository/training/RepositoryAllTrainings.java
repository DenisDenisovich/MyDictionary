package com.dictionary.my.mydictionary.data.repository.training;

import java.util.ArrayList;


/**
 * Created by luxso on 31.03.2018.
 */

public interface RepositoryAllTrainings {
    Integer ENG_RUS_ROWID = 1;
    Integer RUS_ENG_ROWID = 2;
    Integer CONSTRUCTOR_ROWID = 3;
    Integer SPRINT_ROWID = 4;
    Integer FOR_ALL_ROWID = 5;
    int MAX_COUNT_OF_WORDS = 20;
    void setEngRusWords(ArrayList<Long> longs);
    void deleteEngRusWords(ArrayList<Long> longs);
    ArrayList<Long> getEngRusWords();
    Integer getCountOfEngRusWords();

    void setRusEngWords(ArrayList<Long> longs);
    void deleteRusEngWords(ArrayList<Long> longs);
    ArrayList<Long> getRusEngWords();
    Integer getCountOfRusEngWords();

    void setConstructorWords(ArrayList<Long> longs);
    void deleteConstructorWords(ArrayList<Long> longs);
    ArrayList<Long> getConstructorWords();
    Integer getCountOfConstructorWords();

    void setSprintWords(ArrayList<Long> longs);
    void deleteSprintWords(ArrayList<Long> longs);
    ArrayList<Long> getSprintWords();
    Integer getCountOfSprintWords();

    void setAllTrainingWords(ArrayList<Long> longs);
    void deleteAllTrainingWords(ArrayList<Long> longs);
    ArrayList<Long> getAllTrainingWords();
    Integer getCountOfAllTrainingWords();
}
