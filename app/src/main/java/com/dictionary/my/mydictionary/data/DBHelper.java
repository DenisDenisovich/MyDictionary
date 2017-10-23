package com.dictionary.my.mydictionary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luxso on 23.09.2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context) {
        super(context, Content.DB_NAME, null, Content.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table " + Content.TABLE_DICTIONARIES + " ( " +
                Content.COLUMN_TITLE + " text " + ");");
        sqLiteDatabase.execSQL("Create table " + Content.TABLE_ALL_WORD + " ( " +
                                Content.COLUMN_WORD + " text, " +
                                Content.COLUMN_TRANSLATE + " text, " +
                                Content.COLUMN_DICTIONARY + " integer " +");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
