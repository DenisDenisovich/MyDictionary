package com.dictionary.my.mydictionary.domain.training;

import com.dictionary.my.mydictionary.domain.entites.training.WordConstructor;

import io.reactivex.Observable;

/**
 * Created by luxso on 18.02.2018.
 */

public interface UseCaseTrainingConstructor {
    Observable<WordConstructor> getTraining();
    void destroy();
}
