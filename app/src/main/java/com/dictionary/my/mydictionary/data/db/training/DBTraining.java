package com.dictionary.my.mydictionary.data.db.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 18.02.2018.
 */

public interface DBTraining {
    void setEngRusTrainingWords(ArrayList<Long> longs) throws Exception;
    ArrayList<Word> getEngRusTrainingWords() throws Exception;
    Integer getCountOfEngRusTrainingWords() throws Exception;
    void setRusEngTrainingWords(ArrayList<Long> longs) throws Exception;
    ArrayList<Word> getRusEngTrainingWords() throws Exception;
    Integer getCountOfRusEngTrainingWords() throws Exception;
    void setConstructorTrainingWords(ArrayList<Long> longs) throws Exception;
    ArrayList<Word> getConstructorTrainingWords() throws Exception;
    Integer getCountOfConstructorTrainingWords() throws Exception;
    void setSprintTrainingWords(ArrayList<Long> longs) throws Exception;
    ArrayList<Word> getSprintTrainingWords() throws Exception;
    Integer getCountOfSprintTrainingWords() throws Exception;
    void setAllTrainingWords(ArrayList<Long> longs) throws Exception;
    ArrayList<Word> getAllTrainingWords() throws Exception;
    Integer getCountOfAllTrainingWords() throws Exception;
    ArrayList<String> getAllId();
    String getTranslateById(long id);
    String getWordById(long id);
    void destroy();
}
