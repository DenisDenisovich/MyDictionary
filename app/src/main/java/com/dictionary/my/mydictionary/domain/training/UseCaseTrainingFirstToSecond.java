package com.dictionary.my.mydictionary.domain.training;

import com.dictionary.my.mydictionary.data.entites.WordSecondToFirst;


import io.reactivex.Observable;

/**
 * Created by luxso on 23.02.2018.
 */

public interface UseCaseTrainingFirstToSecond {
    Observable<WordSecondToFirst> getTraining();
    void destroy();
}
