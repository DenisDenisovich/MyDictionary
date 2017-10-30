package com.dictionary.my.mydictionary.domain.mappers;

import android.content.ContentValues;

import com.dictionary.my.mydictionary.data.Content;

import java.util.Map;

/**
 * Created by luxso on 30.09.2017.
 */

public class MapToContentValuesAllDictionaries {
    public static ContentValues map(Map<String,Object> m){
        ContentValues cv = new ContentValues();

        String Title = (String) m.get(Content.fromAllDictionaries[1]);

        cv.put(Content.COLUMN_TITLE,Title);

        return cv;
    }
}
