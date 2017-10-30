package com.dictionary.my.mydictionary.view.dictionary;

/**
 * Created by luxso on 30.10.2017.
 */

public interface HostToDictionaryCommand {
    Integer getSizeOfDeleteList();
    void deleteSelectedWords();
    void moveSelectedWords();
    void resetCheckList();
    void editSelectedDictionary();
}
