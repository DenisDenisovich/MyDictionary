package com.dictionary.my.mydictionary.data.db.dictionary;

import android.database.sqlite.SQLiteException;

import com.couchbase.lite.CouchbaseLiteException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 30.09.2017.
 */

public interface DBWords {
    ArrayList<Word> getListOfWord();
    void setNewWord(WordFullInformation word) throws CouchbaseLiteException;
    void setNewWordWithoutInternet(Translation translation) throws CouchbaseLiteException;
    void deleteWords(ArrayList<String> delList);
    void moveWords(ArrayList<String> moveList);
    void destroy();
}
