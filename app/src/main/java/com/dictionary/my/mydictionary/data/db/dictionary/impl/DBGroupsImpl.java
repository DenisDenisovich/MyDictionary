package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.exception.DBException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.db.dictionary.DBGroups;

import java.util.ArrayList;


/**
 * This class working only with Groups table of dataBase
 */

public class DBGroupsImpl implements DBGroups {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBGroupsImpl(Context context){

        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    @Override
    public ArrayList<Group> getListOfGroups() throws Exception{
        ArrayList<Group> list = new ArrayList<>();
        try{
            String[] columns = {Content.COLUMN_ROWID, Content.COLUMN_TITLE};
            Cursor cursor = db.query(Content.TABLE_GROUPS,columns,null,null,null,null,null);
            try {
                if(cursor.moveToLast()){
                    do{
                        Group item = new Group();
                        item.setId(cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID)));
                        item.setTitle(cursor.getString(cursor.getColumnIndex(Content.COLUMN_TITLE)));
                        list.add(item);
                    }while (cursor.moveToPrevious());
                }
            }finally {
                cursor.close();
            }

        }catch (Exception exc){
            throw new DBException(exc);

        }
        return list;
    }

    @Override
    public void setNewGroup(Group group) throws Exception{
        try {
            ContentValues cv = new ContentValues();
            cv.put(Content.COLUMN_TITLE, group.getTitle());
            db.insertOrThrow(Content.TABLE_GROUPS, null, cv);
        }catch (Exception exc){
            throw new DBException(exc);
        }
    }

    @Override
    public void deleteGroups(ArrayList<Long> delList) throws Exception{
        try {
            String strPlaceholder = "(";
            String[] whereArg = new String[delList.size()];
            for (int i = 0; i < delList.size() - 1; i++) {
                strPlaceholder = strPlaceholder.concat("?,");
                whereArg[i] = delList.get(i).toString();
            }
            strPlaceholder = strPlaceholder.concat("?)");
            whereArg[whereArg.length - 1] = delList.get(delList.size() - 1).toString();
            db.delete(Content.TABLE_GROUPS, Content.COLUMN_ROWID + " in " + strPlaceholder, whereArg);
        }catch (Exception exc){
            throw new DBException(exc);
        }
    }

    @Override
    public void editGroup(Group group) throws Exception{
        try {
            ContentValues cv = new ContentValues();
            String idOfModifiedGroup;
            cv.put(Content.COLUMN_TITLE, group.getTitle());
            idOfModifiedGroup = String.valueOf(group.getId());
            db.update(Content.TABLE_GROUPS, cv, Content.COLUMN_ROWID + " = ?", new String[]{idOfModifiedGroup});
        }catch (Exception exc){
            throw new DBException(exc);
        }
    }

    @Override
    public void destroy() {
        db.close();
    }

}
