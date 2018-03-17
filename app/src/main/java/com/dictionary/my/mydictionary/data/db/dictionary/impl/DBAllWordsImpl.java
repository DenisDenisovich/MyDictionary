package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.data.db.dictionary.DBAllWords;

import java.util.ArrayList;
import java.util.List;


/**
 * This class working only with All_Words table of dataBase
 */

public class DBAllWordsImpl implements DBAllWords {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBAllWordsImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public ArrayList<Word> getListOfWord() throws Exception{
        ArrayList<Word> list = new ArrayList<>();
        try{
            String[] column = {Content.COLUMN_ROWID, Content.COLUMN_ENG, Content.COLUMN_RUS, Content.COLUMN_SOUND};
            Cursor cursor = db.query(Content.TABLE_ALL_WORD,column,null,null,null,null,null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Word item = new Word();
                        item.setId(cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID)));
                        item.setWord(cursor.getString(cursor.getColumnIndex(Content.COLUMN_ENG)));
                        item.setTranslate(cursor.getString(cursor.getColumnIndex(Content.COLUMN_RUS)));
                        item.setSound(cursor.getString(cursor.getColumnIndex(Content.COLUMN_SOUND)));
                        list.add(item);
                    } while (cursor.moveToNext());
                }
            }finally {
                cursor.close();
            }
        }catch (Exception exc){
            throw new DBException(exc.toString());
        }
        return list;
    }

    @Override
    public void setNewWord(final WordFullInformation word) throws Exception{
        try {
            ContentValues cv = new ContentValues();
            cv.put(Content.COLUMN_ENG, word.getEng());
            cv.put(Content.COLUMN_RUS, word.getRus());
            cv.put(Content.COLUMN_NOTE, word.getNote());
            cv.put(Content.COLUMN_TRANSCRIPTION, word.getTranscription());
            cv.put(Content.COLUMN_SOUND, word.getSound());
            cv.put(Content.COLUMN_PART_OF_SPEECH, word.getPartOfSpeech());
            cv.put(Content.COLUMN_PREVIEW_IMAGE, word.getPreviewImage());
            cv.put(Content.COLUMN_IMAGE, word.getImage());
            cv.put(Content.COLUMN_DEFINITION, word.getDefinition());
            cv.put(Content.COLUMN_DATE, word.getDate());
            cv.put(Content.COLUMN_GROUP_ID, word.getGroupId());

            String str = "";
            List<Example> examples = word.getExamples();
            for(int i = 0; i < examples.size(); i++){
                str = str.concat(examples.get(i).getText());
                if(i < (examples.size() -1)){
                    str = str.concat(Content.ARRAY_SEPARATOR);
                }
            }
            cv.put(Content.COLUMN_EXAMPLES, str);
            str = "";
            ArrayList<String> alternative = word.getAlternative();
            for(int i = 0; i < alternative.size(); i++){
                str = str.concat(alternative.get(i));
                if(i < (alternative.size() -1)){
                    str = str.concat(Content.ARRAY_SEPARATOR);
                }
            }
            cv.put(Content.COLUMN_ALTERNATIVE, str);
            db.insertOrThrow(Content.TABLE_ALL_WORD,null,cv);
        }catch (Exception exc){
            throw new DBException(exc.toString());
        }
    }

    @Override
    public void setNewWordWithoutInternet(final Translation translation) throws Exception{
        try{
            ContentValues cv = new ContentValues();
            cv.put(Content.COLUMN_ENG, translation.getEng());
            cv.put(Content.COLUMN_RUS, translation.getRus());
            cv.put(Content.COLUMN_GROUP_ID, translation.getGroupId());
            cv.put(Content.COLUMN_DATE, translation.getDate());
            db.insertOrThrow(Content.TABLE_ALL_WORD,null,cv);
        }catch (Exception exc){
            throw new DBException(exc.toString());
        }
    }

    @Override
    public void deleteWords(final ArrayList<Long> delList) throws Exception{
        try {
            String strPlaceholder = "(";
            String[] whereArg = new String[delList.size()];
            for(int i = 0; i < delList.size()-1;i++){
                strPlaceholder = strPlaceholder.concat("?,");
                whereArg[i] = delList.get(i).toString();
            }
            strPlaceholder = strPlaceholder.concat("?)");
            whereArg[whereArg.length-1] = delList.get(delList.size()-1).toString();
            db.delete(Content.TABLE_ALL_WORD,Content.COLUMN_ROWID + " in " + strPlaceholder, whereArg);
        }catch (Exception exc){
            throw new DBException(exc.toString());
        }
    }

    @Override
    public void moveWords(final ArrayList<Long> moveList) throws Exception{
        try {
            Long moveToDictionaryId;
            String strPlaceholder = "(";
            String[] strArg = new String[moveList.size()-1];
            ContentValues cv = new ContentValues();
            moveToDictionaryId = moveList.get(0);
            for(int i = 1; i < moveList.size()-1; i++){
                strPlaceholder = strPlaceholder.concat("?,");
                strArg[i-1] = moveList.get(i).toString();

            }
            strPlaceholder = strPlaceholder.concat("?)");
            strArg[moveList.size()-2] = moveList.get(moveList.size()-1).toString();
            cv.put(Content.COLUMN_GROUP_ID, moveToDictionaryId);
            db.update(Content.TABLE_ALL_WORD, cv, Content.COLUMN_ROWID + " in " + strPlaceholder, strArg);
        }catch (Exception exc){
            throw new DBException(exc.toString());
        }
    }

    @Override
    public void editWord(final Word word) throws Exception{
        try {
            ContentValues cv = new ContentValues();
            String idOfModifiedWord;
            cv.put(Content.COLUMN_RUS, word.getTranslate());
            idOfModifiedWord = String.valueOf(word.getId());
            db.update(Content.TABLE_ALL_WORD,cv,Content.COLUMN_ROWID  + " = ?", new String[] {idOfModifiedWord});
        }catch (Exception exc){
            throw new DBException(exc.toString());
        }
    }

    @Override
    public void destroy() {
        db.close();
    }
}
