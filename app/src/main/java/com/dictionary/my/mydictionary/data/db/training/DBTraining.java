package com.dictionary.my.mydictionary.data.db.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 18.02.2018.
 */

public interface DBTraining {
    void setWordsToTraining(ArrayList<Long> longs, int rowid);
    void deleteWordsFromTraining(ArrayList<Long> longs, int rowid);
    ArrayList<Long> getWordsFromTraining(int rowid);
    Integer getCountOfWordsFromTraining(int rowid);

    void destroy();
}
