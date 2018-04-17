package com.dictionary.my.mydictionary.data.db.training.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.db.training.DBTraining;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class working with training_words table
 */

public class DBTrainingImpl implements DBTraining {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private final static int maxCountOfWords = 20;
    public DBTrainingImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void setEngRusTrainingWords(ArrayList<Long> longs){
        setWordsToTraining(longs, 1);
    }

    @Override
    public ArrayList<Word> getEngRusTrainingWords(){
        String[] id = {String.valueOf(1)};
        Cursor cursor = db.query(Content.TABLE_TRAININGS, new String[] {Content.COLUMN_TRAINING_WORDS_ID}, Content.COLUMN_ROWID + " = ?", id,null,null,null);
        String wordsLine = null;
        try{
            if(cursor.moveToFirst()){
                wordsLine = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRAINING_WORDS_ID));
            }
        }finally {
            cursor.close();
        }
        ArrayList<String> idList = new ArrayList<String>(Arrays.asList(wordsLine.split(Content.ARRAY_SEPARATOR)));
        return null;
    }

    @Override
    public Integer getCountOfEngRusTrainingWords(){
        String[] id = {String.valueOf(1)};
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
    public void setRusEngTrainingWords(ArrayList<Long> longs){
        setWordsToTraining(longs, 2);
    }

    @Override
    public ArrayList<Word> getRusEngTrainingWords(){
        return null;
    }

    @Override
    public Integer getCountOfRusEngTrainingWords(){
        String[] id = {String.valueOf(2)};
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
    public void setConstructorTrainingWords(ArrayList<Long> longs){
        setWordsToTraining(longs, 3);
    }

    @Override
    public ArrayList<Word> getConstructorTrainingWords(){
        return null;
    }

    @Override
    public Integer getCountOfConstructorTrainingWords(){
        String[] id = {String.valueOf(3)};
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
    public void setSprintTrainingWords(ArrayList<Long> longs){
        setWordsToTraining(longs, 4);
    }

    @Override
    public ArrayList<Word> getSprintTrainingWords(){
        return null;
    }

    @Override
    public Integer getCountOfSprintTrainingWords(){
        String[] id = {String.valueOf(4)};
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
    public void setAllTrainingWords(ArrayList<Long> longs){
        setWordsToTraining(longs, 5);
        Cursor cursor = db.query(Content.TABLE_TRAININGS, new String[] {Content.COLUMN_TRAINING_WORDS_ID, Content.COLUMN_TRAINING_COUNT_OF_WORDS}, Content.COLUMN_ROWID + " = 5",null,null,null,null);
        String wordsString = null;
        Integer countOfWords = null;
        try {
            if (cursor.moveToFirst()) {
                wordsString = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRAINING_WORDS_ID));
                countOfWords = cursor.getInt(cursor.getColumnIndex(Content.COLUMN_TRAINING_COUNT_OF_WORDS));
            }
        }finally {
            cursor.close();
        }
        ContentValues cv = new ContentValues();
        cv.put(Content.COLUMN_TRAINING_WORDS_ID, wordsString);
        cv.put(Content.COLUMN_TRAINING_COUNT_OF_WORDS, countOfWords);
        db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = 1", null);
        db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = 2", null);
        db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = 3", null);
        db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = 4", null);
    }

    @Override
    public ArrayList<Word> getAllTrainingWords(){
        return null;
    }

    @Override
    public Integer getCountOfAllTrainingWords(){
        String[] id = {String.valueOf(5)};
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

    private void setWordsToTraining(ArrayList<Long> longs, int rowid){
            Cursor cursor = db.query(Content.TABLE_TRAININGS, null, Content.COLUMN_ROWID + " = ? ", new String[] {String.valueOf(rowid)}, null, null, null);
            String line;
            ArrayList<String> oldWords = null;
            int countOfNewWords = longs.size();
            try {
                if (cursor.moveToFirst()) {
                    line = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRAININGS));
                    if (line != null) {
                        oldWords = new ArrayList<String>(Arrays.asList(line.split(Content.ARRAY_SEPARATOR)));
                    }
                }
            }finally {
                cursor.close();
            }
            ContentValues cv = new ContentValues();
            StringBuilder sb = new StringBuilder();
            if (countOfNewWords == maxCountOfWords) {
                // if size of new words equals maxCount
                // rewrite db value
                for (int i = 0; i < maxCountOfWords - 1; i++) {
                    sb.append(longs.get(i));
                    sb.append(Content.ARRAY_SEPARATOR);
                }
                sb.append(longs.get(maxCountOfWords - 1));
                cv.put(Content.COLUMN_TRAINING_WORDS_ID, sb.toString());
                cv.put(Content.COLUMN_TRAINING_COUNT_OF_WORDS, maxCountOfWords);
                db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = ?", new String[] {String.valueOf(rowid)});
            } else {
                // if size of words not equals maxCount
                int count = 0;
                // add new words
                for (int i = 0; i < longs.size(); i++) {
                    sb.append(longs.get(i));
                    sb.append(Content.ARRAY_SEPARATOR);
                    count++;
                }
                // add oldWords that aren't in newWords
                if (oldWords != null) {
                    if (!oldWords.isEmpty()) {
                        for (int i = 0; i < oldWords.size(); i++) {
                            if (count < maxCountOfWords) {
                                if (!longs.contains(Long.valueOf(oldWords.get(i)))) {
                                    sb.append(longs.get(i));
                                    if (count < maxCountOfWords - 1) {
                                        sb.append(Content.ARRAY_SEPARATOR);
                                    }
                                    count++;
                                }
                            }
                        }
                    }
                }
                cv.put(Content.COLUMN_TRAINING_WORDS_ID, sb.toString());
                cv.put(Content.COLUMN_TRAINING_COUNT_OF_WORDS, count);
                db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " = ? ", new String[] {String.valueOf(rowid)});
            }
            cv.clear();
    }

    private Integer getCountOfTrainingWords(int rowId){
        Integer count = 0;

        return count;
    }
    @Override
    public ArrayList<String> getAllId() {
        /*Cursor cursor = db.rawQuery("select " + Content.COLUMN_ROWID + " from " + Content.TABLE_ALL_WORD,null);
        ArrayList<String> ids = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                ids.add(cursor.getString(cursor.getColumnIndex(Content.COLUMN_ROWID)));
            }while (cursor.moveToNext());
        }
        return ids;*/
        return null;
    }

    @Override
    public String getTranslateById(long id) {
        /*String translate;
        Cursor cursor = db.rawQuery("select " + Content.COLUMN_TRANSLATE + " from " + Content.TABLE_ALL_WORD + " where " +
                Content.COLUMN_ROWID + " = ? ",new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()){
            translate = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRANSLATE));
            cursor.close();
            return translate;
        }*/
        return null;
    }

    @Override
    public String getWordById(long id) {
       /* Cursor cursor = db.rawQuery("select " + Content.COLUMN_WORD + " from " + Content.TABLE_ALL_WORD + " where " +
                Content.COLUMN_ROWID + " = ? ",new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(Content.COLUMN_WORD));
        }*/
        return null;
    }

    @Override
    public void destroy() {
        db.close();
    }
}
