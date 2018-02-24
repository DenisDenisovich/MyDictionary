package com.dictionary.my.mydictionary.domain.dictionary.mappers;

import android.database.Cursor;

import com.dictionary.my.mydictionary.data.Content;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luxso on 30.09.2017.
 */

public class CursorToMapAllDictionaries {
    public static Map<String,Object> map(Cursor cursor){
        Map<String,Object> item = new HashMap<>();

        long RowID = cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID));
        String Title = cursor.getString(cursor.getColumnIndex(Content.COLUMN_TITLE));

        item.put(Content.fromAllDictionaries[0],RowID);
        item.put(Content.fromAllDictionaries[1],Title);

        return item;
    }
}
