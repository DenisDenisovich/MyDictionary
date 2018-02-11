package com.dictionary.my.mydictionary.domain;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by luxso on 01.02.2018.
 */

public interface UseCaseTrainingWordTranslate {
    Observable<ArrayList<Map<String,Object>>> getTraining();
    void destroy();
}
