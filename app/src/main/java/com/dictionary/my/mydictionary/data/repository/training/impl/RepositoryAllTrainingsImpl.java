package com.dictionary.my.mydictionary.data.repository.training.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.db.training.DBTraining;
import com.dictionary.my.mydictionary.data.db.training.impl.DBTrainingImpl;
import com.dictionary.my.mydictionary.data.repository.training.RepositoryAllTrainings;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by luxso on 31.03.2018.
 */

public class RepositoryAllTrainingsImpl implements RepositoryAllTrainings {
    private DBTraining db;
    public RepositoryAllTrainingsImpl(Context context){
        db = new DBTrainingImpl(context);
    }


    @Override
    public Single<Integer> getCountOfEngRusWords() {
        return Single.create(e -> {
            Integer count = db.getCountOfWordsFromTraining(ENG_RUS_ROWID);
            if(!e.isDisposed()){
                e.onSuccess(count);
            }
        });
    }

    @Override
    public Single<Integer> getCountOfRusEngWords() {
        return Single.create(e -> {
            Integer count = db.getCountOfWordsFromTraining(RUS_ENG_ROWID);
            if(!e.isDisposed()){
                e.onSuccess(count);
            }
        });
    }

    @Override
    public Single<Integer> getCountOfConstructorWords() {
        return Single.create(e -> {
            Integer count = db.getCountOfWordsFromTraining(CONSTRUCTOR_ROWID);
            if(!e.isDisposed()){
                e.onSuccess(count);
            }
        });
    }

    @Override
    public Single<Integer> getCountOfSprintWords() {
        return Single.create(e -> {
            Integer count = db.getCountOfWordsFromTraining(SPRINT_ROWID);
            if(!e.isDisposed()){
                e.onSuccess(count);
            }
        });
    }

    @Override
    public void destroy() {
        db.destroy();
    }
}
