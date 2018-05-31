package com.dictionary.my.mydictionary.data.db.dictionary;

import com.couchbase.lite.CouchbaseLiteException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

/**
 * Created by luxso on 31.05.2018.
 */

public interface DBFullInfoWord {
    WordFullInformation getWord(String id) throws CouchbaseLiteException;
    void destroy();
}
