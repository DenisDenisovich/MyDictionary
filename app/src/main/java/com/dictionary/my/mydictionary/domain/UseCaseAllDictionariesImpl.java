package com.dictionary.my.mydictionary.domain;

import android.content.Context;

import com.dictionary.my.mydictionary.data.repository.storage.AllDictionariesRepositories;
import com.dictionary.my.mydictionary.data.repository.storage.AllDictionariesRepositoriesImpl;


import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 29.09.2017.
 */

public class UseCaseAllDictionariesImpl implements UseCaseAllDictionaries{
    AllDictionariesRepositories repositories;
    public UseCaseAllDictionariesImpl(Context context){
        repositories = new AllDictionariesRepositoriesImpl(context);
    }
    public Observable<Map<String,Object>> getDictionariesList(){
        return repositories.getDictionariesList();
    }
    public void setNewDictionary(Single<Map<String,Object>> observable){
        repositories.setNewDictionary(observable);
    }
    public void deleteDictionaries(Single<ArrayList<Long>> observable){
        repositories.deleteDictionaries(observable);
    }

    @Override
    public void editDictionary(Single<Map<String, Object>> observable) {
        repositories.editDictionary(observable);

    }

    @Override
    public void destroy()
    {
        repositories.destroy();
    }
}
