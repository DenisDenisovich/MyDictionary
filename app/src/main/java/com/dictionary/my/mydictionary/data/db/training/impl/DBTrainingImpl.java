package com.dictionary.my.mydictionary.data.db.training.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.db.training.DBTraining;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class working with training_words table
 */

public class DBTrainingImpl implements DBTraining {
    private final static String LOG_TAG = "Log_DBTraining";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    public DBTrainingImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void setWordsToTraining(ArrayList<Long> longs, int rowid){
        ContentValues cv = new ContentValues();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < longs.size()-1; i++){
            sb.append(longs.get(i));
            sb.append(Content.ARRAY_SEPARATOR);
        }
        sb.append(longs.get(longs.size()-1));
        cv.put(Content.COLUMN_TRAINING_WORDS_ID, sb.toString());
        cv.put(Content.COLUMN_TRAINING_COUNT_OF_WORDS, longs.size());
        db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = ?", new String[] {String.valueOf(rowid)});
        cv.clear();
    }

    public void deleteWordsFromTraining(ArrayList<Long> longs, int rowid){
        Cursor cursor = db.query(Content.TABLE_TRAININGS, null, Content.COLUMN_ROWID + " = ? ", new String[] {String.valueOf(rowid)}, null, null, null);
        String line;
        ArrayList<String> oldWords = null;
        try {
            if (cursor.moveToFirst()) {
                line = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRAINING_WORDS_ID));
                if (line != null) {
                    oldWords = new ArrayList<String>(Arrays.asList(line.split(Content.ARRAY_SEPARATOR)));
                }
            }
        }finally {
            cursor.close();
        }
        for(int  i = 0; i < longs.size(); i++){
            oldWords.remove(String.valueOf(longs.get(i)));

        }
        ContentValues cv = new ContentValues();
        StringBuilder sb = new StringBuilder();
        if(!oldWords.isEmpty()) {
            for (int i = 0; i < oldWords.size() - 1; i++) {
                sb.append(oldWords.get(i));
                sb.append(Content.ARRAY_SEPARATOR);
            }
            sb.append(oldWords.get(oldWords.size() - 1));
        }
        cv.put(Content.COLUMN_TRAINING_WORDS_ID, sb.toString());
        cv.put(Content.COLUMN_TRAINING_COUNT_OF_WORDS, oldWords.size());
        db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = ?", new String[] {String.valueOf(rowid)});
    }

    @Override
    public ArrayList<Long> getWordsFromTraining(int rowid) {
        String[] id = {String.valueOf(rowid)};
        Cursor cursor = db.query(Content.TABLE_TRAININGS, new String[]{Content.COLUMN_TRAINING_WORDS_ID}, Content.COLUMN_ROWID + " = ?", id, null, null, null);
        String wordsLine = null;
        try {
            if (cursor.moveToFirst()) {
                wordsLine = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRAINING_WORDS_ID));
            }
        } finally {
            cursor.close();
        }
        ArrayList<Long> idsList = new ArrayList<>();
        ArrayList<String> strIds = new ArrayList<String>(Arrays.asList(wordsLine.split(Content.ARRAY_SEPARATOR)));
        try {
            for (String str : strIds) {
                idsList.add(Long.valueOf(str));
            }
        }catch (NumberFormatException e){
            // if reading string is empty
            return idsList;
        }
        return idsList;
    }

    @Override
    public Integer getCountOfWordsFromTraining(int rowid) {
        String[] id = {String.valueOf(rowid)};
        Cursor cursor = db.query(Content.TABLE_TRAININGS,new String[] {Content.COLUMN_TRAINING_COUNT_OF_WORDS},Content.COLUMN_ROWID + " = ? ",id,null,null,null);
        Integer count = null;
        try {
            if(cursor.moveToFirst()){
                count = cursor.getInt(cursor.getColumnIndex(Content.COLUMN_TRAINING_COUNT_OF_WORDS));
            }
        }finally {
            cursor.close();
        }
        return count;
    }

    @Override
    public ArrayList<Long> getListOfTrainings() {
        Cursor cursor = db.query(Content.TABLE_TRAININGS,new String[] {Content.COLUMN_ROWID},null,null,null,null,null);
        ArrayList<Long> trainings = new ArrayList<>();
        try {
            if(cursor.moveToFirst()){
                do {
                    trainings.add(cursor.getLong(cursor.getColumnIndex(Content.COLUMN_ROWID)));
                }while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }
        return trainings;
    }

    @Override
    public void destroy() {
        db.close();
    }
}
