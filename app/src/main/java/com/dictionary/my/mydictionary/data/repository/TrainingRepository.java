package com.dictionary.my.mydictionary.data.repository;

import java.util.ArrayList;

/**
 * Created by luxso on 18.02.2018.
 */

public interface TrainingRepository {
    ArrayList<String> getAllId();
    String getTranslateById(long id);
    String getWordById(long id);
    void destroy();
}
