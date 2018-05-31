package com.dictionary.my.mydictionary.data.repository.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import io.reactivex.Single;

/**
 * Created by luxso on 31.05.2018.
 */

public interface RepositoryFullInfoWord {
    Single<WordFullInformation> getWord(String id);
    void destroy();
}
