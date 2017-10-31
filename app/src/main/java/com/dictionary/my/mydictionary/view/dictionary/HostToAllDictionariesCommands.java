package com.dictionary.my.mydictionary.view.dictionary;

/**
 * Created by luxso on 13.10.2017.
 */

public interface HostToAllDictionariesCommands {
    Integer getSizeOfDeleteList();
    void deleteSelectedDictionaries();
    void resetCheckList();
    void editSelectedDictionary();
}
