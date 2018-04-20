package com.dictionary.my.mydictionary.data.repository.training.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.db.training.DBTraining;
import com.dictionary.my.mydictionary.data.db.training.impl.DBTrainingImpl;
import com.dictionary.my.mydictionary.data.repository.training.RepositoryAllTrainings;

import java.util.ArrayList;

/**
 * Created by luxso on 31.03.2018.
 */

public class RepositoryAllTrainingsImpl implements RepositoryAllTrainings {
    private DBTraining db;
    public RepositoryAllTrainingsImpl(Context context){
        db = new DBTrainingImpl(context);
    }
    @Override
    public void setEngRusWords(ArrayList<Long> longs) {
        if(longs.size() == MAX_COUNT_OF_WORDS){
            db.setWordsToTraining(longs,ENG_RUS_ROWID);
        }else {
            // if size of input words not equals maxCount
            ArrayList<Long> oldWords = db.getWordsFromTraining(ENG_RUS_ROWID);
            ArrayList<Long> newWords = new ArrayList<>(longs);
            int count = newWords.size();
            // add oldWords that aren't in newWords
            if (oldWords != null) {
                if (!oldWords.isEmpty()) {
                    for (int i = 0; i < oldWords.size(); i++) {
                        if (count <= MAX_COUNT_OF_WORDS) {
                            if (!longs.contains(oldWords.get(i))) {
                                newWords.add(oldWords.get(i));
                                count++;
                            }
                        }
                    }
                }
            }
            db.setWordsToTraining(newWords,ENG_RUS_ROWID);
        }
    }

    @Override
    public void deleteEngRusWords(ArrayList<Long> longs) {

    }

    @Override
    public ArrayList<Long> getEngRusWords() {
        return db.getWordsFromTraining(ENG_RUS_ROWID);
    }

    @Override
    public Integer getCountOfEngRusWords() {
        return null;
    }

    @Override
    public void setRusEngWords(ArrayList<Long> longs) {

    }

    @Override
    public void deleteRusEngWords(ArrayList<Long> longs) {

    }

    @Override
    public ArrayList<Long> getRusEngWords() {
        return null;
    }

    @Override
    public Integer getCountOfRusEngWords() {
        return null;
    }

    @Override
    public void setConstructorWords(ArrayList<Long> longs) {

    }

    @Override
    public void deleteConstructorWords(ArrayList<Long> longs) {

    }

    @Override
    public ArrayList<Long> getConstructorWords() {
        return null;
    }

    @Override
    public Integer getCountOfConstructorWords() {
        return null;
    }

    @Override
    public void setSprintWords(ArrayList<Long> longs) {

    }

    @Override
    public void deleteSprintWords(ArrayList<Long> longs) {

    }

    @Override
    public ArrayList<Long> getSprintWords() {
        return null;
    }

    @Override
    public Integer getCountOfSprintWords() {
        return null;
    }

    @Override
    public void setAllTrainingWords(ArrayList<Long> longs) {

    }

    @Override
    public void deleteAllTrainingWords(ArrayList<Long> longs) {

    }

    @Override
    public ArrayList<Long> getAllTrainingWords() {
        return null;
    }

    @Override
    public Integer getCountOfAllTrainingWords() {
        return null;
    }
}
