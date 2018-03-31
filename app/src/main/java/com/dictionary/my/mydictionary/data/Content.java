package com.dictionary.my.mydictionary.data;

/**
 * Created by luxso on 29.09.2017.
 */

public interface Content {
    String DB_NAME = "MyDictionaryDataBase";
    int DB_VERSION = 1;

    String TABLE_GROUPS = "dictionaries";
    String COLUMN_ROWID = "rowid";
    String COLUMN_TITLE = "title";

    String TABLE_ALL_WORD = "all_word";
    String COLUMN_ENG = "eng";
    String COLUMN_RUS = "rus";
    String COLUMN_NOTE = "note";
    String COLUMN_TRANSCRIPTION = "transcription";
    String COLUMN_SOUND = "sound";
    String COLUMN_PART_OF_SPEECH = "part_of_speech";
    String COLUMN_PREVIEW_IMAGE = "preview_image";
    String COLUMN_IMAGE = "image";
    String COLUMN_DEFINITION = "definition";
    String COLUMN_EXAMPLES = "examples";
    String COLUMN_ALTERNATIVE = "alternative";
    String COLUMN_DATE = "date";
    String COLUMN_GROUP_ID = "group_id";

    String ARRAY_SEPARATOR = "___,___";


//    final String[] fromAllDictionaries = {COLUMN_ROWID, COLUMN_TITLE};
    //final String[] fromDictionary = {COLUMN_ROWID,COLUMN_WORD,COLUMN_TRANSLATE};

//    final String selectDbAllDictionaries = "select " + COLUMN_ROWID + ", " + COLUMN_TITLE + " " +
  //                                          "from " + TABLE_DICTIONARIES;
   // final String deleteDbAllDictionaries = COLUMN_ROWID + " = ";
   // final String deleteDbAllDictionariesWithWords = COLUMN_DICTIONARY + " = ";
   // final String editDbAllDictionaries = COLUMN_ROWID + " = ";

    //final String selectDbDictionary = "select " + COLUMN_ROWID + ", " + COLUMN_WORD + ", " + COLUMN_TRANSLATE + " " +
      //                                 "from " + TABLE_ALL_WORD + " where " + COLUMN_DICTIONARY + " = ? ";

   // final String deleteDbDictionary = COLUMN_ROWID + " = ";
   // final String editDbDictionary = COLUMN_ROWID + " = ";
}
