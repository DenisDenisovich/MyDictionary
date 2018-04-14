package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudWords;
import com.dictionary.my.mydictionary.data.cloud.dictionary.impl.CloudWordsImpl;
import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.data.exception.SkyEngWordException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryWords;
import com.dictionary.my.mydictionary.data.db.dictionary.DBGroups;
import com.dictionary.my.mydictionary.data.db.dictionary.DBWords;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBGroupsImpl;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBWordsImpl;

import java.io.IOException;
import java.sql.SQLException;
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

public class RepositoryWordsImpl implements RepositoryWords {
    private final static String LOG_TAG = "Log_RepositoryAllWords";
    private DBWords dbWords;
    private DBGroups dbGroups;
    private CloudWords cloudWords;

    public RepositoryWordsImpl(Context context){
        dbWords = new DBWordsImpl(context);
        dbGroups = new DBGroupsImpl(context);
        cloudWords = new CloudWordsImpl();
    }
    public RepositoryWordsImpl(Context context, Long groupId){
        dbWords = new DBWordsImpl(context, groupId);
        dbGroups = new DBGroupsImpl(context);
        cloudWords = new CloudWordsImpl();
    }
    @Override
    public Single<ArrayList<Word>> getListOfWords() {
        Log.d(LOG_TAG, "getListOfWords()");
        return Single.create(new SingleOnSubscribe<ArrayList<Word>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Word>> e) throws Exception {
                try {
                    ArrayList<Word> words = dbWords.getListOfWord();
                    if(!e.isDisposed()){
                        e.onSuccess(words);
                    }
                }catch (SQLiteException exc){
                    Log.d(LOG_TAG, "Exception in getListOfWords()");
                    exc.printStackTrace();
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        Log.d(LOG_TAG, "getListOfGroups()");
        return Single.create(new SingleOnSubscribe<ArrayList<Group>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Group>> e) throws Exception {
                try {
                    ArrayList<Group> groups = dbGroups.getListOfGroups();
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
        Log.d(LOG_TAG, "getTranslation()");
        return Single.create(new SingleOnSubscribe<ArrayList<Translation>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Translation>> e) throws Exception {
                try {
                    ArrayList<Translation> translations = cloudWords.getTranslation(word);
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
        Log.d(LOG_TAG, "setNewWord()");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    WordFullInformation wordFullInformation = cloudWords.getMeaning(translation);
                    dbWords.setNewWord(wordFullInformation);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SkyEngWordException skyEngExc){
                    // if we are cant getting full information about word by Internet
                    skyEngExc.printStackTrace();
                    try {
                        dbWords.setNewWordWithoutInternet(translation);
                        if(!e.isDisposed()){
                            e.onComplete();
                        }
                    }catch (SQLException sqlExc){
                        Log.d(LOG_TAG, "Exception of db.setNewWordWithoutInternet()");
                        sqlExc.printStackTrace();
                        if(!e.isDisposed()) {
                            e.onError(sqlExc);
                        }
                    }
                }catch (SQLException sqlExc){
                    Log.d(LOG_TAG, "Exception of db.setNewWord()");
                    sqlExc.printStackTrace();
                    if(!e.isDisposed()) {
                        e.onError(sqlExc);
                    }
                }

            }
        });
    }

    @Override
    public Completable setNewWordWithoutInternet(final Translation translation) {
        Log.d(LOG_TAG, "setNewWordWithoutInternet()");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try{
                    dbWords.setNewWordWithoutInternet(translation);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SQLException exc){
                    Log.d(LOG_TAG, "Exception of db.setNewWordWithoutInternet()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Completable deleteWords(final ArrayList<Long> delList) {
        Log.d(LOG_TAG, "deleteWords()");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try{
                    dbWords.deleteWords(delList);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SQLiteException exc){
                    Log.d(LOG_TAG, "Exception of db.deleteWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                } catch (NullPointerException exc){
                    Log.d(LOG_TAG, "Exception of db.deleteWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                } catch (IndexOutOfBoundsException exc){
                    Log.d(LOG_TAG, "Exception of db.deleteWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }

            }
        });
    }

    @Override
    public Completable moveWords(final ArrayList<Long> moveList) {
        Log.d(LOG_TAG, "moveWords()");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    dbWords.moveWords(moveList);
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (SQLiteException exc){
                    Log.d(LOG_TAG, "Exception of db.moveWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }catch (NullPointerException exc){
                    Log.d(LOG_TAG, "Exception of db.moveWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                } catch (IndexOutOfBoundsException exc){
                    Log.d(LOG_TAG, "Exception of db.moveWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                } catch (NegativeArraySizeException exc){
                    Log.d(LOG_TAG, "Exception of db.moveWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public void destroy() {
        Log.d(LOG_TAG, "destroy()");
        dbWords.destroy();
        dbGroups.destroy();
    }
}
