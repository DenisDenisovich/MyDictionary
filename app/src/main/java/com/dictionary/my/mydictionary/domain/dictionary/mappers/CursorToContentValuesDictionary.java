package com.dictionary.my.mydictionary.domain.dictionary.mappers;

import android.content.ContentValues;
import android.database.Cursor;

import com.dictionary.my.mydictionary.data.Content;

/**
 * Created by luxso on 05.11.2017.
 */

public class CursorToContentValuesDictionary {
    public static ContentValues map(Cursor cursor, Long dictionaryId){
        ContentValues cv = new ContentValues();
        String RowID = cursor.getString(cursor.getColumnIndex(Content.COLUMN_ROWID));
       // String Word = cursor.getString(cursor.getColumnIndex(Content.COLUMN_WORD));
        //String Translate = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRANSLATE));

       // cv.put(Content.fromDictionary[0],RowID);
       // cv.put(Content.fromDictionary[1],Word);
       // cv.put(Content.fromDictionary[2],Translate);
       // cv.put(Content.COLUMN_DICTIONARY,dictionaryId.toString());

        return cv;
    }
}
