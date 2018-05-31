package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.dictionary.my.mydictionary.data.CBKeys;
import com.dictionary.my.mydictionary.data.DataBaseManager;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.data.db.dictionary.DBFullInfoWord;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxso on 31.05.2018.
 */

public class DBFullInfoWordImpl implements DBFullInfoWord {
    private final static String LOG_TAG = "Log_DBFullInfoWord";
    private Database db;
    public DBFullInfoWordImpl(Context context){
        db = DataBaseManager.getSharedInstance(context).databaseWords;
    }
    @Override
    public WordFullInformation getWord(String id)throws CouchbaseLiteException {
        Log.d(LOG_TAG, "getWord()");
        WordFullInformation word = new WordFullInformation();
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property(CBKeys.KEY_ID).equalTo(Expression.string(id)));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary d = result.getDictionary(CBKeys.DB_WORDS);
            word.setEng(d.getString(CBKeys.KEY_ENG));
            word.setRus(d.getString(CBKeys.KEY_RUS));
            if (d.contains(CBKeys.KEY_SOUND)){
                word.setSound(d.getString(CBKeys.KEY_SOUND));
            }
            if (d.contains(CBKeys.KEY_NOTE)){
                word.setNote(d.getString(CBKeys.KEY_NOTE));
            }
            if (d.contains(CBKeys.KEY_TRANSCRIPTION)){
                word.setTranscription(d.getString(CBKeys.KEY_TRANSCRIPTION));
            }
            if (d.contains(CBKeys.KEY_PART_OF_SPEECH)){
                word.setPartOfSpeech(d.getString(CBKeys.KEY_PART_OF_SPEECH));
            }
            if (d.contains(CBKeys.KEY_IMAGE)){
                word.setImage(d.getString(CBKeys.KEY_IMAGE));
            }
            if (d.contains(CBKeys.KEY_DEFINITION)){
                word.setDefinition(d.getString(CBKeys.KEY_DEFINITION));
            }
            if(d.contains(CBKeys.KEY_EXAMPLES)){
                List<Example> examples = new ArrayList<>();
                for(Object s: d.getArray(CBKeys.KEY_EXAMPLES)) {
                    Example e = new Example();
                    e.setText(s.toString());
                    examples.add(e);
                }
                word.setExamples(examples);
            }
        }
        return word;
    }

    @Override
    public void destroy() {

    }
}
