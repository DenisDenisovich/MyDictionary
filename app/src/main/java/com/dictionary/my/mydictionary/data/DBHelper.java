package com.dictionary.my.mydictionary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luxso on 23.09.2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    private final static String LOG_TAG = "Log_DBHelper";
    public DBHelper(Context context) {
        super(context, Content.DB_NAME, null, Content.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG, "onCreate()");
        sqLiteDatabase.execSQL("Create table " + Content.TABLE_GROUPS + " ( " +
                Content.COLUMN_TITLE + " text " + ");");
        ContentValues cv = new ContentValues();
        cv.put(Content.COLUMN_TITLE,"Without group");
        sqLiteDatabase.insert(Content.TABLE_GROUPS,null,cv);

        sqLiteDatabase.execSQL("Create table " + Content.TABLE_ALL_WORD + " ( " +
                                Content.COLUMN_ENG + " text, " +
                                Content.COLUMN_RUS + " text, " +
                                Content.COLUMN_NOTE + " text, " +
                                Content.COLUMN_TRANSCRIPTION + " text, " +
                                Content.COLUMN_SOUND + " text, " +
                                Content.COLUMN_PART_OF_SPEECH + " text, " +
                                Content.COLUMN_PREVIEW_IMAGE + " text, " +
                                Content.COLUMN_IMAGE + " text, " +
                                Content.COLUMN_DEFINITION + " text, " +
                                Content.COLUMN_EXAMPLES + " text, " +
                                Content.COLUMN_ALTERNATIVE + " text, " +
                                Content.COLUMN_DATE + " text, " +
                                Content.COLUMN_GROUP_ID + " integer, " +
                                Content.COLUMN_COUNT_OF_RIGHT_ANSWER + " text " + ");");

        sqLiteDatabase.execSQL("Create table " + Content.TABLE_TRAININGS + " ( " +
                Content.COLUMN_TRAININGS + " text, " +
                Content.COLUMN_TRAINING_WORDS_ID + " test " + ");");
        cv.clear();
        cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_ENG_RUS);
        sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
        cv.clear();
        cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_RUS_ENG);
        sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
        cv.clear();
        cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_CONSTRUCTOR);
        sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
        cv.clear();
        cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_SPRINT);
        sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
        cv.clear();
        cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_FOR_ALL);
        sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
        cv.clear();


        sqLiteDatabase.execSQL("alter table " + Content.TABLE_ALL_WORD + " add column " + Content.COLUMN_COUNT_OF_RIGHT_ANSWER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(LOG_TAG, "onUpgrade()");
        if(i == 6 && i1 == 7) {
            sqLiteDatabase.beginTransaction();
            try {

                sqLiteDatabase.execSQL("Drop table " + Content.TABLE_TRAININGS);
                sqLiteDatabase.execSQL("Create table " + Content.TABLE_TRAININGS + " ( " +
                        Content.COLUMN_TRAININGS + " text, " +
                        Content.COLUMN_TRAINING_WORDS_ID + " test " + ");");
                ContentValues cv = new ContentValues();
                cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_ENG_RUS);
                sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
                cv.clear();
                cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_RUS_ENG);
                sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
                cv.clear();
                cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_CONSTRUCTOR);
                sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
                cv.clear();
                cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_SPRINT);
                sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
                cv.clear();
                cv.put(Content.COLUMN_TRAININGS, Content.COLUMN_TRAININGS_ITEM_FOR_ALL);
                sqLiteDatabase.insert(Content.TABLE_TRAININGS, null, cv);
                cv.clear();

                sqLiteDatabase.setTransactionSuccessful();
            }finally {
                sqLiteDatabase.endTransaction();
            }

        }
    }
}
