package com.dictionary.my.mydictionary.view.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 04.03.2018.
 */

public interface ViewAllWords {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void createList(ArrayList<Word> words);
    void setListOfGroups(ArrayList<Group> groups);
    Word getNewWord();
    ArrayList<Long> getDeletedWords();
    Integer getTopVisiblePosition();
    void setTopVisiblePosition(Integer position);
    ArrayList<Long> getMovedWords();
    Word getEditedWord();
}
