package com.dictionary.my.mydictionary.data.repository;


import java.util.ArrayList;

/**
 * Created by luxso on 01.02.2018.
 */

public interface TrainingWordTranslateRepository {
    ArrayList<String> getAllId();
    String getTranslateById(long id);
    String getWordById(long id);
    void destroy();
}
