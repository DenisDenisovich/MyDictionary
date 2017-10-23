package com.dictionary.my.mydictionary.view;

/**
 * Created by luxso on 27.09.2017.
 */

public interface DictionaryListener {
    void setDictionary(long dictionaryId);
    void openNewDictionary();
    long getDictionary();

    void startChangeItemMod();
    void endChangeItemMod();
    void setDeletedItemCount(int deletedCount);
}
