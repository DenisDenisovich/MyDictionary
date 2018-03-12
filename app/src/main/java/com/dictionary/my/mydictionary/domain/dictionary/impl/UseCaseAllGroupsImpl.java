package com.dictionary.my.mydictionary.domain.dictionary.impl;

import com.dictionary.my.mydictionary.domain.dictionary.UseCaseAllGroups;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 11.03.2018.
 */

public class UseCaseAllGroupsImpl implements UseCaseAllGroups {
    @Override
    public Observable<Map<String, Object>> getDictionariesList() {
        return null;
    }

    @Override
    public void setNewDictionary(Single<Map<String, Object>> observable) {

    }

    @Override
    public void deleteDictionaries(Single<ArrayList<Long>> observable) {

    }

    @Override
    public void editDictionary(Single<Map<String, Object>> observable) {

    }

    @Override
    public void destroy() {

    }
}
