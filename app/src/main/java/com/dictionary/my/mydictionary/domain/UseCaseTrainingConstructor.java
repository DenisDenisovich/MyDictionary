package com.dictionary.my.mydictionary.domain;

import com.dictionary.my.mydictionary.domain.entites.WordConstructor;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by luxso on 18.02.2018.
 */

public interface UseCaseTrainingConstructor {
    Observable<WordConstructor> getTraining();
    void destroy();
}
