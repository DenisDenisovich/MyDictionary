package com.dictionary.my.mydictionary.data.db.dictionary;

import android.database.sqlite.SQLiteException;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import java.sql.SQLException;
import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 30.09.2017.
 */

public interface DBWords {
    ArrayList<Word> getListOfWord() throws SQLiteException, IllegalStateException;
    void setNewWord(WordFullInformation word) throws SQLException;
    void setNewWordWithoutInternet(Translation translation) throws SQLException;
    void deleteWords(ArrayList<Long> delList) throws SQLiteException, NullPointerException, IndexOutOfBoundsException;
    void moveWords(ArrayList<Long> moveList) throws SQLiteException, NullPointerException, IndexOutOfBoundsException, NegativeArraySizeException;
    void destroy();
}
