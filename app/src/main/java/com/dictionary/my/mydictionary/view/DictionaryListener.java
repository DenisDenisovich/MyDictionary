package com.dictionary.my.mydictionary.view;

/**
 * Created by luxso on 27.09.2017.
 */

public interface DictionaryListener {
    void selectedDictionary(long dictionaryId);
    long getDictionary();
    void checkListIsChange();
}
