package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudWords;
import com.dictionary.my.mydictionary.data.cloud.dictionary.impl.CloudWordsImpl;
import com.dictionary.my.mydictionary.data.db.training.DBTraining;
import com.dictionary.my.mydictionary.data.db.training.impl.DBTrainingImpl;
import com.dictionary.my.mydictionary.data.exception.MeaningIsNotFoundException;
import com.dictionary.my.mydictionary.data.exception.TranslationIsNotFoundException;
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
    private DBTraining dbTraining;
    private CloudWords cloudWords;

    public RepositoryWordsImpl(Context context){
        dbWords = new DBWordsImpl(context);
        dbGroups = new DBGroupsImpl(context);
        cloudWords = new CloudWordsImpl();
        dbTraining = new DBTrainingImpl(context);
    }
    public RepositoryWordsImpl(Context context, Long groupId){
        dbWords = new DBWordsImpl(context, groupId);
        dbGroups = new DBGroupsImpl(context);
        cloudWords = new CloudWordsImpl();
        dbTraining = new DBTrainingImpl(context);
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
                }catch (SQLiteException | IllegalStateException exc){
                    Log.d(LOG_TAG, "Exception in getListOfWords()");
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
                }catch (SQLiteException exc){
                    Log.d(LOG_TAG, "Exception in getListOfGroups()");
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
                }catch (TranslationIsNotFoundException | IOException exc){
                    Log.d(LOG_TAG, "Exception in getTranslation()");
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
                }catch (MeaningIsNotFoundException | IOException skyEngExc){
                    // if we are cant getting full information about word by Internet
                    Log.d(LOG_TAG, "MeaningIsNotFound");
                    try {
                        dbWords.setNewWordWithoutInternet(translation);
                        if(!e.isDisposed()){
                            e.onComplete();
                        }
                    }catch (SQLException sqlExc){
                        Log.d(LOG_TAG, "Exception of db.setNewWordWithoutInternet()");
                        if(!e.isDisposed()) {
                            e.onError(sqlExc);
                        }
                    }
                }catch (SQLException sqlExc){
                    Log.d(LOG_TAG, "Exception of db.setNewWord()");
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
                }catch (SQLiteException | NullPointerException | IndexOutOfBoundsException exc){
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
                }catch (SQLiteException | NullPointerException | NegativeArraySizeException | IndexOutOfBoundsException exc){
                    Log.d(LOG_TAG, "Exception of db.moveWords()");
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
    }

    @Override
    public Single<ArrayList<Long>> getListOfTrainings() {
        return null;
    }

    @Override
    public Completable setWordsToTraining(ArrayList<Long> longs, int rowid) {
        Log.d(LOG_TAG, "setWordsToTraining(" + rowid + ")");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    if (longs.size() == MAX_COUNT_OF_WORDS_IN_TRAINIGS) {
                        dbTraining.setWordsToTraining(longs, rowid);
                        if (rowid == FOR_ALL_TRAININGS_ROWID) {
                            dbTraining.setWordsToTraining(longs, ENG_RUS_TRAINING_ROWID);
                            dbTraining.setWordsToTraining(longs, RUS_ENG_TRAINING_ROWID);
                            dbTraining.setWordsToTraining(longs, CONSTRUCTOR_TRAINING_ROWID);
                            dbTraining.setWordsToTraining(longs, SPRINT_TRAINING_ROWID);
                        }
                    } else {
                        // if size of input words not equals maxCount
                        ArrayList<Long> oldWords = dbTraining.getWordsFromTraining(rowid);
                        ArrayList<Long> newWords = new ArrayList<>(longs);
                        int count = newWords.size();
                        // add oldWords that aren't in newWords
                        if (oldWords != null) {
                            if (!oldWords.isEmpty()) {
                                for (int i = 0; i < oldWords.size(); i++) {
                                    if (count <= MAX_COUNT_OF_WORDS_IN_TRAINIGS) {
                                        if (!longs.contains(oldWords.get(i))) {
                                            newWords.add(oldWords.get(i));
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                        dbTraining.setWordsToTraining(newWords, rowid);
                        if (rowid == FOR_ALL_TRAININGS_ROWID) {
                            dbTraining.setWordsToTraining(newWords, ENG_RUS_TRAINING_ROWID);
                            dbTraining.setWordsToTraining(newWords, RUS_ENG_TRAINING_ROWID);
                            dbTraining.setWordsToTraining(newWords, CONSTRUCTOR_TRAINING_ROWID);
                            dbTraining.setWordsToTraining(newWords, SPRINT_TRAINING_ROWID);
                        }
                    }
                    if (!e.isDisposed()) {
                        e.onComplete();
                    }
                }catch (SQLiteException | NullPointerException | IndexOutOfBoundsException exc){
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
