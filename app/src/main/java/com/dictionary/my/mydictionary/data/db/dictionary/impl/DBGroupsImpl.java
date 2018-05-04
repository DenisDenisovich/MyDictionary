package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.dictionary.my.mydictionary.data.CBKeys;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.DataBaseManager;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.db.dictionary.DBGroups;

import java.util.ArrayList;
import java.util.Date;


/**
 * This class working only with dbGroups CouchBase Lite database
 */

public class DBGroupsImpl implements DBGroups {
    private static final String LOG_TAG = "Log_DBGroups";
    private Database db;

    public DBGroupsImpl(Context context){
        db = DataBaseManager.getSharedInstance(context).databaseGroups;
    }
    @Override
    public ArrayList<Group> getListOfGroups(){
        ArrayList<Group> list = new ArrayList<>();
        Query query = QueryBuilder
                .select(SelectResult.property(CBKeys.KEY_ID),
                        SelectResult.property(CBKeys.KEY_TITLE))
                .from(DataSource.database(db))
                .orderBy(Ordering.property(CBKeys.KEY_DATE));
        try {

            ResultSet rs = query.execute();
            for (Result result : rs) {
                Group item = new Group();
                item.setId(result.getString(CBKeys.KEY_ID));
                item.setTitle(result.getString(CBKeys.KEY_TITLE));
                list.add(item);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void setNewGroup(Group group) throws CouchbaseLiteException {
        MutableDocument newGroup = new MutableDocument();
        newGroup.setString(CBKeys.KEY_TYPE, CBKeys.GROUP_TYPE);
        newGroup.setString(CBKeys.KEY_TITLE, group.getTitle());
        newGroup.setDate(CBKeys.KEY_DATE, new Date());
        db.save(newGroup);
    }

    @Override
    public void deleteGroups(ArrayList<String> delList){
        for(String id:delList) {
            Document group = db.getDocument(id);
            try {
                db.delete(group);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void editGroup(Group group) throws CouchbaseLiteException {
        MutableDocument word = db.getDocument(group.getId()).toMutable();
        word.setString(CBKeys.KEY_TITLE,group.getTitle());
        db.save(word);
    }

    @Override
    public void destroy() {
        //db.close();
    }

}
