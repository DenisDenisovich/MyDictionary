package com.dictionary.my.mydictionary.domain.dictionary.mappers;

import android.database.Cursor;

import com.dictionary.my.mydictionary.data.Content;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luxso on 30.09.2017.
 */

public class CursorToMapDictionary {
    public static Map<String,Object> map(Cursor cursor){
        Map<String,Object> item = new HashMap<>();

        long RowID = cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID));
        String Word = cursor.getString(cursor.getColumnIndex(Content.COLUMN_WORD));
        String Translate = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TRANSLATE));

        item.put(Content.fromDictionary[0],RowID);
        item.put(Content.fromDictionary[1],Word);
        item.put(Content.fromDictionary[2],Translate);

        return item;
    }
}
