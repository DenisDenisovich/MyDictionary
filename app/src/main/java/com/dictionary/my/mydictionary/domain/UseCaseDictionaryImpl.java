package com.dictionary.my.mydictionary.domain;

import android.content.Context;

import com.dictionary.my.mydictionary.data.repository.DictionaryRepository;
import com.dictionary.my.mydictionary.data.repository.DictionaryRepositoryImpl;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 23.09.2017.
 */

public class UseCaseDictionaryImpl implements UseCaseDictionary{
    DictionaryRepository repository;
    public UseCaseDictionaryImpl(Context context, Long currentDictionaryId){
        repository = new DictionaryRepositoryImpl(context, currentDictionaryId);
    }


    @Override
    public Observable<Map<String, Object>> getWordsList() {
        return repository.getWordList();
    }

    @Override
    public Observable<Map<String, Object>> getDictionaryList() {
        return repository.getDictionaryList();
    }

    @Override
    public void setNewWord(Single<Map<String, Object>> observable) {
        repository.setNewWord(observable);
    }

    @Override
    public void deleteWords(Single<ArrayList<Long>> observable) {
        repository.deleteWords(observable);
    }

    @Override
    public void moveWords(Observable<Long> observable) {
        repository.moveWords(observable);
    }

    @Override
    public void editWord(Single<Map<String, Object>> observable) {
        repository.editWord(observable);
    }

    @Override
    public void destroy() {
        repository.destroy();
    }
}
