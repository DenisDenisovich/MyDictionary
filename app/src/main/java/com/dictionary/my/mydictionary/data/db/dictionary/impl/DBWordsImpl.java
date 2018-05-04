package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Array;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.dictionary.my.mydictionary.data.CBKeys;
import com.dictionary.my.mydictionary.data.DataBaseManager;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.db.dictionary.DBWords;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This class working only with dbWords CouchBase Lite database
 */

public class DBWordsImpl implements DBWords {
    private final static String LOG_TAG = "Log_DBAllWords";
    private Database db;
    private String groupId = null;

    public DBWordsImpl(Context context){
        db = DataBaseManager.getSharedInstance(context).databaseWords;
    }

    public DBWordsImpl(Context context, String groupId){
        this.groupId = groupId;
        db = DataBaseManager.getSharedInstance(context).databaseWords;
    }


    @Override
    public ArrayList<Word> getListOfWord(){
        Log.d(LOG_TAG, "getListOfWords()");
        ArrayList<Word> list = new ArrayList<>();
        Query query;
        if(groupId == null) {
            query = QueryBuilder
                    .select(SelectResult.property(CBKeys.KEY_ID),
                            SelectResult.property(CBKeys.KEY_ENG),
                            SelectResult.property(CBKeys.KEY_RUS),
                            SelectResult.property(CBKeys.KEY_SOUND))
                    .from(DataSource.database(db))
                    .orderBy(Ordering.property(CBKeys.KEY_DATE).descending());
        }else {
            query = QueryBuilder
                    .select(SelectResult.property(CBKeys.KEY_ID),
                            SelectResult.property(CBKeys.KEY_ENG),
                            SelectResult.property(CBKeys.KEY_RUS),
                            SelectResult.property(CBKeys.KEY_SOUND))
                    .from(DataSource.database(db))
                    .where(Expression.property(CBKeys.KEY_GROUP_ID).equalTo(Expression.string(groupId)))
                    .orderBy(Ordering.property(CBKeys.KEY_DATE).descending());
        }
        try {
            ResultSet rs = query.execute();
            Log.d(LOG_TAG, "get words ");
            for (Result result : rs) {
                Word word = new Word();
                word.setId(result.getString(CBKeys.KEY_ID));
                word.setWord(result.getString(CBKeys.KEY_ENG));
                word.setTranslate(result.getString(CBKeys.KEY_RUS));
                Log.d(LOG_TAG, "id: " + word.getId());
                Log.d(LOG_TAG, "eng: " + word.getWord());
                Log.d(LOG_TAG, "rus: " + word.getTranslate());
                if (result.contains(CBKeys.KEY_SOUND)){
                    word.setSound(result.getString(CBKeys.KEY_SOUND));
                }
                list.add(word);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void setNewWord(final WordFullInformation word) throws CouchbaseLiteException {
        Log.d(LOG_TAG, "setNewWord()");
        MutableDocument newWord = new MutableDocument();
        newWord.setString(CBKeys.KEY_TYPE, CBKeys.WORD_TYPE);
        newWord.setString(CBKeys.KEY_ENG, word.getEng());
        newWord.setString(CBKeys.KEY_RUS, word.getRus());
        newWord.setString(CBKeys.KEY_NOTE, word.getNote());
        newWord.setString(CBKeys.KEY_TRANSCRIPTION, word.getTranscription());
        newWord.setString(CBKeys.KEY_SOUND, word.getSound());
        newWord.setString(CBKeys.KEY_PART_OF_SPEECH, word.getPartOfSpeech());
        newWord.setString(CBKeys.KEY_PREVIEW_IMAGE, word.getPreviewImage());
        newWord.setString(CBKeys.KEY_IMAGE, word.getImage());
        newWord.setString(CBKeys.KEY_DEFINITION, word.getDefinition());
        newWord.setDate(CBKeys.KEY_DATE, new Date());
        newWord.setString(CBKeys.KEY_GROUP_ID, word.getGroupId());
        if(word.getExamples() != null){
            MutableArray exampleArray = new MutableArray();
            List<Example> examples = word.getExamples();
            for (Example e : examples) {
                exampleArray.addString(e.getText());
            }
            newWord.setArray(CBKeys.KEY_EXAMPLES, exampleArray);
        }

        if(word.getAlternative() != null){
            MutableArray alternativeArray = new MutableArray();
            List<String> alternative = word.getAlternative();
            for (String a : alternative) {
                alternativeArray.addString(a);
            }
            newWord.setArray(CBKeys.KEY_ALTERNATIVE, alternativeArray);
        }

        db.save(newWord);
    }

    @Override
    public void setNewWordWithoutInternet(final Translation translation) throws CouchbaseLiteException {
        Log.d(LOG_TAG, "setNewWordWithoutInternet()");
        MutableDocument newWord = new MutableDocument();
        newWord.setString(CBKeys.KEY_TYPE, CBKeys.WORD_TYPE);
        newWord.setString(CBKeys.KEY_ENG, translation.getEng());
        newWord.setString(CBKeys.KEY_RUS, translation.getRus());
        newWord.setString(CBKeys.KEY_GROUP_ID, translation.getGroupId());
        newWord.setDate(CBKeys.KEY_DATE, new Date());
        if(translation.getSound() != null){
            newWord.setString(CBKeys.KEY_SOUND,translation.getSound());
        }
        if(translation.getPreview_image() != null){
            newWord.setString(CBKeys.KEY_PREVIEW_IMAGE,translation.getPreview_image());
        }

        db.save(newWord);
    }

    @Override
    public void deleteWords(final ArrayList<String> delList){
        Log.d(LOG_TAG, "deleteWords()");
        for(String id:delList) {
            Document word = db.getDocument(id);
            try {
                db.delete(word);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void moveWords(final ArrayList<String> moveList){
        Log.d(LOG_TAG, "moveWords()");
        String moveToDictionaryId = moveList.get(0);
        for(int i = 1; i < moveList.size();i++) {
            MutableDocument word = db.getDocument(moveList.get(i)).toMutable();
            try {
                word.setString(CBKeys.KEY_GROUP_ID,moveToDictionaryId);
                db.save(word);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void destroy() {
        //db.close();
    }
}
