package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.dictionary.my.mydictionary.data.db.dictionary.DBFullInfoWord;
import com.dictionary.my.mydictionary.data.db.dictionary.impl.DBFullInfoWordImpl;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryFullInfoWord;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by luxso on 31.05.2018.
 */

public class RepositoryFullInfoWordImpl implements RepositoryFullInfoWord {
    DBFullInfoWord db;
    public RepositoryFullInfoWordImpl(Context context){
        db = new DBFullInfoWordImpl(context);
    }

    @Override
    public Single<WordFullInformation> getWord(String id) {
        return Single.create(e -> {
            try{
                WordFullInformation word = db.getWord(id);
                if(!e.isDisposed()){
                    e.onSuccess(word);
                }
            }catch (CouchbaseLiteException exc){
                //exc.printStackTrace();
                if(!e.isDisposed()){
                    e.onError(exc);
                }
            }
        });
    }

    @Override
    public void destroy() {
        db.destroy();
    }
}
