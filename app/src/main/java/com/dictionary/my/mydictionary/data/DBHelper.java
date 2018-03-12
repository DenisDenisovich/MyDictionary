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
        sqLiteDatabase.execSQL("Create table " + Content.TABLE_GROUPS + " ( " +
                Content.COLUMN_TITLE + " text " + ");");
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
                                Content.COLUMN_DEFINITION + " text, " +
                                Content.COLUMN_EXAMPLES + " text, " +
                                Content.COLUMN_ALTERNATIVE + " text, " +
                                Content.COLUMN_DATE + " text, " +
                                Content.COLUMN_GROUP_ID + " integer " +");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
