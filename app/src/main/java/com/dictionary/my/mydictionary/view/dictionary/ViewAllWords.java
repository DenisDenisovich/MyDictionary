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
    ArrayList<String> getSelectedItemIds();
    Boolean getSelectMode();
    void setSelectedItemIds(ArrayList<String> selectedItemIds);
    void setSelectMode(Boolean selectMode);
    void createDeleteDialog();
    ArrayList<String> getDeletedWords();
    void deleteWordsFromList();
    void createMoveToGroupDialog(ArrayList<Group> groups);
    ArrayList<String> getMovedToGroupWords();
    void moveWordsFromList();
    void createMoveToTrainingDialog(ArrayList<Long> ids);
    ArrayList<Long> getMovedToTrainingWords();
    Integer getSelectedTraining();
    void allowMoveToTraining();
}
