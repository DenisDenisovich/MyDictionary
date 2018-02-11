package com.dictionary.my.mydictionary.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;

import java.util.ArrayList;

/**
 * Created by luxso on 01.02.2018.
 */

public class TrainingWordTranslateRepositoryImpl implements TrainingWordTranslateRepository {
    DBHelper dbHelper;
    SQLiteDatabase db;
    public TrainingWordTranslateRepositoryImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();

    }
    @Override
    public ArrayList<String> getAllId() {
        Cursor cursor = db.rawQuery("select " + Content.COLUMN_ROWID + " from " + Content.TABLE_ALL_WORD,null);
        ArrayList<String> ids = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                ids.add(cursor.getString(cursor.getColumnIndex(Content.COLUMN_ROWID)));
            }while (cursor.moveToNext());
        }
        return ids;
    }

    @Override
    public String getTranslateById(long id) {
        Cursor cursor = db.rawQuery("select " + Content.COLUMN_TRANSLATE + " from " + Content.TABLE_ALL_WORD + " where " +
                Content.COLUMN_ROWID + " = ? ",new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRANSLATE));
        }
        return null;
    }

    @Override
    public String getWordById(long id) {
        Cursor cursor = db.rawQuery("select " + Content.COLUMN_WORD + " from " + Content.TABLE_ALL_WORD + " where " +
        Content.COLUMN_ROWID + " = ? ",new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(Content.COLUMN_WORD));
        }
        return null;
    }

    @Override
    public void destroy() {
        db.close();
    }
}
