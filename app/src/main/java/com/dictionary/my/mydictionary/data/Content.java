package com.dictionary.my.mydictionary.data;

/**
 * Created by luxso on 29.09.2017.
 */

public interface Content {
    final String DB_NAME = "MyDictionaryDataBase";
    final int DB_VERSION = 1;
    final String TABLE_DICTIONARIES = "dictionaries";
    final String COLUMN_TITLE = "title";
    final String TABLE_ALL_WORD = "all_word";
    final String COLUMN_ROWID = "rowid";
    final String COLUMN_WORD = "word";
    final String COLUMN_TRANSLATE = "translate";
    final String COLUMN_DICTIONARY = "dictionary";

    final String[] fromAllDictionaries = {COLUMN_ROWID, COLUMN_TITLE};
    final String[] fromDictionary = {COLUMN_ROWID,COLUMN_WORD,COLUMN_TRANSLATE};

    final String selectDbAllDictionaries = "select " + COLUMN_ROWID + ", " + COLUMN_TITLE + " " +
                                            "from " + TABLE_DICTIONARIES;
    final String deleteDbAllDictionaries = COLUMN_ROWID + " = ";
    final String editDbAllDictionaries = COLUMN_ROWID + " = ";
}
