package com.dictionary.my.mydictionary.domain.training;

import com.dictionary.my.mydictionary.data.entites.training.WordSecondToFirst;

import io.reactivex.Observable;

/**
 * Created by luxso on 01.02.2018.
 */

public interface UseCaseTrainingSecondToFirst {
    Observable<WordSecondToFirst> getTraining();
    void destroy();
}
