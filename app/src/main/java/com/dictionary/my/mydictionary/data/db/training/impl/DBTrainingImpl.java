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
 * Created by luxso on 18.02.2018.
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
    public void setEngRusTrainingWords(ArrayList<Long> longs) throws Exception {
        setWordsToTraining(longs, 1);
    }

    @Override
    public ArrayList<Word> getEngRusTrainingWords() throws Exception {
        return null;
    }

    @Override
    public Integer getCountOfEngRusTrainingWords() throws Exception {
        return null;
    }

    @Override
    public void setRusEngTrainingWords(ArrayList<Long> longs) throws Exception {
        setWordsToTraining(longs, 2);
    }

    @Override
    public ArrayList<Word> getRusEngTrainingWords() throws Exception {
        return null;
    }

    @Override
    public Integer getCountOfRusEngTrainingWords() throws Exception {
        return null;
    }

    @Override
    public void setConstructorTrainingWords(ArrayList<Long> longs) throws Exception {
        setWordsToTraining(longs, 3);
    }

    @Override
    public ArrayList<Word> getConstructorTrainingWords() throws Exception {
        return null;
    }

    @Override
    public Integer getCountOfConstructorTrainingWords() throws Exception {
        return null;
    }

    @Override
    public void setSprintTrainingWords(ArrayList<Long> longs) throws Exception {
        setWordsToTraining(longs, 4);
    }

    @Override
    public ArrayList<Word> getSprintTrainingWords() throws Exception {
        return null;
    }

    @Override
    public Integer getCountOfSprintTrainingWords() throws Exception {
        return null;
    }

    @Override
    public void setAllTrainingWords(ArrayList<Long> longs) throws Exception {
        setWordsToTraining(longs, 1);
        setWordsToTraining(longs, 2);
        setWordsToTraining(longs, 3);
        setWordsToTraining(longs, 4);
        setWordsToTraining(longs, 5);
    }

    @Override
    public ArrayList<Word> getAllTrainingWords() throws Exception {
        return null;
    }

    @Override
    public Integer getCountOfAllTrainingWords() throws Exception {
        return null;
    }

    private void setWordsToTraining(ArrayList<Long> longs, int rowid) throws Exception{
            Cursor cursor = db.query(Content.TABLE_TRAININGS, null, Content.COLUMN_ROWID + " == " + String.valueOf(rowid), null, null, null, null);
            String line = null;
            ArrayList<String> oldWords = null;
            int countOfNewWords = longs.size();
            if (cursor.moveToFirst()) {
                line = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRAININGS));
                if (line != null) {
                    oldWords = new ArrayList<String>(Arrays.asList(line.split(Content.ARRAY_SEPARATOR)));
                }
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
                sb.append(longs.get(maxCountOfWords));
                cv.put(Content.COLUMN_TRAINING_WORDS_ID, sb.toString());
                cv.put(Content.COLUMN_TRAINING_COUNT_OF_WORDS, maxCountOfWords);
                db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " == " + String.valueOf(rowid), null);
            } else {
                // if size of words not equals maxCount
                int count = 0;
                // add new words
                for (int i = 0; i < longs.size(); i++) {
                    sb.append(longs.get(i));
                    sb.append(Content.ARRAY_SEPARATOR);
                    count++;
                }
                // add oldWords which not exist in newWords
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
                db.update(Content.TABLE_TRAININGS, cv, Content.COLUMN_ROWID + " == " + String.valueOf(rowid), null);
            }
            cursor.close();
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
