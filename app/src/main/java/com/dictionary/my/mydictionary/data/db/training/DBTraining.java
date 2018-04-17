package com.dictionary.my.mydictionary.data.db.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 18.02.2018.
 */

public interface DBTraining {
    void setEngRusTrainingWords(ArrayList<Long> longs);
    ArrayList<Word> getEngRusTrainingWords();
    Integer getCountOfEngRusTrainingWords();
    void setRusEngTrainingWords(ArrayList<Long> longs);
    ArrayList<Word> getRusEngTrainingWords();
    Integer getCountOfRusEngTrainingWords();
    void setConstructorTrainingWords(ArrayList<Long> longs);
    ArrayList<Word> getConstructorTrainingWords();
    Integer getCountOfConstructorTrainingWords();
    void setSprintTrainingWords(ArrayList<Long> longs);
    ArrayList<Word> getSprintTrainingWords();
    Integer getCountOfSprintTrainingWords();
    void setAllTrainingWords(ArrayList<Long> longs);
    ArrayList<Word> getAllTrainingWords();
    Integer getCountOfAllTrainingWords();
    ArrayList<String> getAllId();
    String getTranslateById(long id);
    String getWordById(long id);
    void destroy();
}
