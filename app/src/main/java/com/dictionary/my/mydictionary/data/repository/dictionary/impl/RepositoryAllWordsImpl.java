package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.data.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllWords;
import com.dictionary.my.mydictionary.data.storage.dictionary.DBAllGroups;
import com.dictionary.my.mydictionary.data.storage.dictionary.DBAllWords;
import com.dictionary.my.mydictionary.data.storage.dictionary.impl.DBAllGroupsImpl;
import com.dictionary.my.mydictionary.data.storage.dictionary.impl.DBAllWordsImpl;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 11.03.2018.
 */

public class RepositoryAllWordsImpl implements RepositoryAllWords {
    private DBAllWords dbAllWords;
    private DBAllGroups dbAllGroups;

    public RepositoryAllWordsImpl(Context context){
        dbAllWords = new DBAllWordsImpl(context);
        dbAllGroups = new DBAllGroupsImpl(context);
    }
    @Override
    public Single<ArrayList<Word>> getListOfWords() {
        return null;
    }

    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        return null;
    }

    @Override
    public Observable<Translation> getTranslation() {
        return null;
    }

    @Override
    public void setNewWord(Single<WordFullInformation> observable) {

    }

    @Override
    public void deleteWords(Single<ArrayList<Long>> observable) {

    }

    @Override
    public void moveWords(Single<ArrayList<Long>> observable) {

    }

    @Override
    public void editWord(Single<Word> observable) {

    }

    @Override
    public void destroy() {

    }
}
