package com.dictionary.my.mydictionary.domain.training;


import com.dictionary.my.mydictionary.data.entites.WordSprint;

import io.reactivex.Observable;

/**
 * Created by luxso on 19.02.2018.
 */

public interface UseCaseTrainingSprint {
    Observable<WordSprint> getTraining();
    void destroy();
}
