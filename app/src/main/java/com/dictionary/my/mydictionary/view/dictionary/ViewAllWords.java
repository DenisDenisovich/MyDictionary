package com.dictionary.my.mydictionary.view.dictionary;

import com.dictionary.my.mydictionary.data.entites.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 04.03.2018.
 */

public interface ViewAllWords {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void createList(ArrayList<Word> data);
    void setListOfGroups(/* arrayList of group class*/);
    Word getNewWord();
    ArrayList<Long> getDeletedWords();
    Long getTopVisiblePosition();
    ArrayList<Long> getMovedWords();
    Word getEditedWord();
}
