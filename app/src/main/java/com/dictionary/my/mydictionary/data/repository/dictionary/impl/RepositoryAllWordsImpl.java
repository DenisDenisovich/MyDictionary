package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudAllWords;
import com.dictionary.my.mydictionary.data.cloud.dictionary.impl.CloudAllWordsImpl;
import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.data.exception.SkyEngWordException;
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

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * This class manages all necessary data for AllWords
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
        return Single.create(new SingleOnSubscribe<ArrayList<Word>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Word>> e) throws Exception {
                try {
                    ArrayList<Word> words = dbAllWords.getListOfWord();
                    if(!e.isDisposed()){
                        e.onSuccess(words);
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        return Single.create(new SingleOnSubscribe<ArrayList<Group>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Group>> e) throws Exception {
                try {
                    ArrayList<Group> groups = dbAllGroups.getListOfGroups();
                    if(!e.isDisposed()){
                        e.onSuccess(groups);
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()){
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Single<ArrayList<Translation>> getTranslation(final String word) {

        return Single.create(new SingleOnSubscribe<ArrayList<Translation>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Translation>> e) throws Exception {
                try {
                    ArrayList<Translation> translations = cloudAllWords.getTranslation(word);
                    if(!e.isDisposed()){
                        e.onSuccess(translations);
                    }
                }catch (SkyEngWordException exc){
                    if(!e.isDisposed()){
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable setNewWord(final Translation translation) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    WordFullInformation wordFullInformation = cloudAllWords.getMeaning(translation);
                    dbAllWords.setNewWord(wordFullInformation);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SkyEngWordException skyEngExc){
                    // if we are cant getting full information about word by Internet
                    try {
                        dbAllWords.setNewWordWithoutInternet(translation);
                    }catch (DBException dbExc){
                        if(!e.isDisposed()) {
                            e.onError(dbExc);
                        }
                    }
                }catch (DBException dbExc){
                    if(!e.isDisposed()) {
                        e.onError(dbExc);
                    }
                }

            }
        });
    }

    @Override
    public Completable setNewWordWithoutInternet(final Translation translation) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try{
                    dbAllWords.setNewWordWithoutInternet(translation);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable deleteWords(final ArrayList<Long> delList) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try{
                    dbAllWords.deleteWords(delList);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable moveWords(final ArrayList<Long> moveList) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    dbAllWords.moveWords(moveList);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable editWord(final Word word) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    dbAllWords.editWord(word);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (DBException exc){
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public void destroy() {
        dbAllWords.destroy();
        dbAllGroups.destroy();
    }
}
