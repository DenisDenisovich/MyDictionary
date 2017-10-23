package com.dictionary.my.mydictionary.view.dictionary;

/**
 * Created by luxso on 13.10.2017.
 */

public interface HostToAllDictionariesCommands {
    int getDeletedItemCount();
    void deleteSelectedDictionaries();
    void deleteSelectedDictionariesWithData();
    void cancelDeletedDictionaries();
    void editSelectedDictionary();
}
