package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudAllWords;
import com.dictionary.my.mydictionary.data.cloud.dictionary.impl.CloudAllWordsImpl;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllWords;
import com.dictionary.my.mydictionary.data.db.dictionary.DBAllGroups;
import com.dictionary.my.mydictionary.data.db.dictionary.DBAllWords;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBAllGroupsImpl;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBAllWordsImpl;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by luxso on 11.03.2018.
 */

public class RepositoryAllWordsImpl implements RepositoryAllWords {
    private DBAllWords dbAllWords;
    private DBAllGroups dbAllGroups;
    private CloudAllWords cloudAllWords;

    public RepositoryAllWordsImpl(Context context){
        dbAllWords = new DBAllWordsImpl(context);
        dbAllGroups = new DBAllGroupsImpl(context);
        cloudAllWords = new CloudAllWordsImpl();
    }
    @Override
    public Single<ArrayList<Word>> getListOfWords() {
        return dbAllWords.getListOfWord();
    }

    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        return dbAllGroups.getListOfGroups();
    }

    @Override
    public Single<ArrayList<Translation>> getTranslation(final String word) {

        return Single.create(new SingleOnSubscribe<ArrayList<Translation>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Translation>> e) throws Exception {
                ArrayList<Translation> translations = cloudAllWords.getTranslation(word);
                try {
                    if(!e.isDisposed()){
                        e.onSuccess(translations);
                    }
                }catch (Throwable t){
                    if(!e.isDisposed()){
                        e.onError(t);
                    }
                }
            }
        });
    }

    @Override
    public void setNewWord(Single<Translation> observable) {

        //dbAllWords.setNewWord(observable);
    }

    @Override
    public void deleteWords(Single<ArrayList<Long>> observable) {
        dbAllWords.deleteWords(observable);
    }

    @Override
    public void moveWords(Single<ArrayList<Long>> observable) {
        dbAllWords.moveWords(observable);
    }

    @Override
    public void editWord(Single<Word> observable) {
        dbAllWords.editWord(observable);
    }

    @Override
    public void destroy() {
        dbAllWords.destroy();
        dbAllGroups.destroy();
    }
}
