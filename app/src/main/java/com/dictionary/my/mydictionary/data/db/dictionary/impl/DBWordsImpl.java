package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.data.db.dictionary.DBWords;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class working only with All_Words table of dataBase
 */

public class DBWordsImpl implements DBWords {
    private final static String LOG_TAG = "Log_DBAllWords";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Long groupId = null;

    public DBWordsImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public DBWordsImpl(Context context, Long groupId){
        this.groupId = groupId;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public ArrayList<Word> getListOfWord(){
        Log.d(LOG_TAG, "getListOfWords()");
        ArrayList<Word> list = new ArrayList<>();
        String[] columns = {Content.COLUMN_ROWID, Content.COLUMN_ENG, Content.COLUMN_RUS, Content.COLUMN_SOUND};
        Cursor cursor;
        if(groupId != null){
            String whereClause = Content.COLUMN_GROUP_ID + " = ?";
            String[] whereArg = {String.valueOf(groupId)};
            cursor = db.query(Content.TABLE_ALL_WORD,columns,whereClause,whereArg,null,null,null);
        }else {
            cursor = db.query(Content.TABLE_ALL_WORD,columns,null,null,null,null,null);
        }
        try {
            if (cursor.moveToLast()) {
                do {
                    Word item = new Word();
                    item.setId(cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID)));
                    item.setWord(cursor.getString(cursor.getColumnIndex(Content.COLUMN_ENG)));
                    item.setTranslate(cursor.getString(cursor.getColumnIndex(Content.COLUMN_RUS)));
                    item.setSound(cursor.getString(cursor.getColumnIndex(Content.COLUMN_SOUND)));
                    list.add(item);
                } while (cursor.moveToPrevious());
            }
        }finally {
            cursor.close();

        }
        return list;
    }

    @Override
    public void setNewWord(final WordFullInformation word){
        Log.d(LOG_TAG, "setNewWord()");
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

        String strExample = "";
        try {
            List<Example> examples = word.getExamples();
            for (int i = 0; i < examples.size(); i++) {
                strExample = strExample.concat(examples.get(i).getText());
                if (i < (examples.size() - 1)) {
                    strExample = strExample.concat(Content.ARRAY_SEPARATOR);
                }
            }
            cv.put(Content.COLUMN_EXAMPLES, strExample);
        }catch (IndexOutOfBoundsException indexExc){
            Log.d(LOG_TAG, "Exception of word.getExamples()");
            indexExc.printStackTrace();
            strExample = "";
            cv.put(Content.COLUMN_EXAMPLES, strExample);
        }catch (NullPointerException nullExc){
            Log.d(LOG_TAG, "Exception of word.getExamples()");
            nullExc.printStackTrace();
            strExample = "";
            cv.put(Content.COLUMN_EXAMPLES, strExample);
        }

        String strAlternative = "";
        try {
            ArrayList<String> alternative = word.getAlternative();
            for (int i = 0; i < alternative.size(); i++) {
                strAlternative = strAlternative.concat(alternative.get(i));
                if (i < (alternative.size() - 1)) {
                    strAlternative = strAlternative.concat(Content.ARRAY_SEPARATOR);
                }
            }
            cv.put(Content.COLUMN_ALTERNATIVE, strAlternative);
        }catch (IndexOutOfBoundsException indexExc){
            Log.d(LOG_TAG, "Exception of word.getAlternative()");
            indexExc.printStackTrace();
            strAlternative = "";
            cv.put(Content.COLUMN_ALTERNATIVE, strAlternative);
        }catch (NullPointerException nullExc){
            Log.d(LOG_TAG, "Exception of word.getAlternative()");
            nullExc.printStackTrace();
            strAlternative = "";
            cv.put(Content.COLUMN_ALTERNATIVE, strAlternative);
        }

        /*Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_ENG));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_RUS));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_NOTE));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_TRANSCRIPTION));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_SOUND));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_PART_OF_SPEECH));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_PREVIEW_IMAGE));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_IMAGE));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_DEFINITION));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_DATE));
        Log.d(LOG_TAG,String.valueOf(cv.get(Content.COLUMN_GROUP_ID)));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_EXAMPLES));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_ALTERNATIVE));*/

        db.insertOrThrow(Content.TABLE_ALL_WORD,null,cv);
    }

    @Override
    public void setNewWordWithoutInternet(final Translation translation){
        Log.d(LOG_TAG, "setNewWordWithoutInternet()");
        ContentValues cv = new ContentValues();
        cv.put(Content.COLUMN_ENG, translation.getEng());
        cv.put(Content.COLUMN_RUS, translation.getRus());
        cv.put(Content.COLUMN_GROUP_ID, translation.getGroupId());
        cv.put(Content.COLUMN_DATE, translation.getDate());

        /*Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_ENG));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_RUS));
        Log.d(LOG_TAG,(String)cv.get(Content.COLUMN_DATE));
        Log.d(LOG_TAG,String.valueOf(cv.get(Content.COLUMN_GROUP_ID)));*/

        db.insertOrThrow(Content.TABLE_ALL_WORD,null,cv);
    }

    @Override
    public void deleteWords(final ArrayList<Long> delList){
        Log.d(LOG_TAG, "deleteWords()");
        String strPlaceholder = "(";
        String[] whereArg = new String[delList.size()];
        for(int i = 0; i < delList.size()-1;i++){
            strPlaceholder = strPlaceholder.concat("?,");
            whereArg[i] = delList.get(i).toString();
        }
        strPlaceholder = strPlaceholder.concat("?)");
        whereArg[whereArg.length-1] = delList.get(delList.size()-1).toString();
        db.delete(Content.TABLE_ALL_WORD,Content.COLUMN_ROWID + " in " + strPlaceholder, whereArg);
    }

    @Override
    public void moveWords(final ArrayList<Long> moveList){
        Log.d(LOG_TAG, "moveWords()");
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
    }

    @Override
    public void destroy() {
        db.close();
    }
}
