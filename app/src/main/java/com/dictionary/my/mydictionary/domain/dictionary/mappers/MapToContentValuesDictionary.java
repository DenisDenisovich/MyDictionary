package com.dictionary.my.mydictionary.domain.dictionary.mappers;

import android.content.ContentValues;

import com.dictionary.my.mydictionary.data.Content;

import java.util.Map;

/**
 * Created by luxso on 30.09.2017.
 */

public class MapToContentValuesDictionary {
    public static ContentValues map(Map<String,Object> m){
        ContentValues cv = new ContentValues();

        String Word = (String) m.get(Content.fromDictionary[1]);
        String Translate = (String) m.get(Content.fromDictionary[2]);

        cv.put(Content.COLUMN_WORD,Word);
        cv.put(Content.COLUMN_TRANSLATE,Translate);

        return cv;
    }
}
