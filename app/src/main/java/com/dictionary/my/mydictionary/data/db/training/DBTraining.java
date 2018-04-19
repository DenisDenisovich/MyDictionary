package com.dictionary.my.mydictionary.data.db.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 18.02.2018.
 */

public interface DBTraining {
    void setEngRusTrainingWords(ArrayList<Long> longs);
    ArrayList<Long> getEngRusTrainingWords();
    Integer getCountOfEngRusTrainingWords();
    void setRusEngTrainingWords(ArrayList<Long> longs);
    ArrayList<Long> getRusEngTrainingWords();
    Integer getCountOfRusEngTrainingWords();
    void setConstructorTrainingWords(ArrayList<Long> longs);
    ArrayList<Long> getConstructorTrainingWords();
    Integer getCountOfConstructorTrainingWords();
    void setSprintTrainingWords(ArrayList<Long> longs);
    ArrayList<Long> getSprintTrainingWords();
    Integer getCountOfSprintTrainingWords();
    void setAllTrainingWords(ArrayList<Long> longs);
    ArrayList<Long> getAllTrainingWords();
    Integer getCountOfAllTrainingWords();
    ArrayList<String> getAllId();
    String getTranslateById(long id);
    String getWordById(long id);
    void destroy();
}
