package com.dictionary.my.mydictionary.view.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 04.03.2018.
 */

public interface AllWordsFragment {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void createList(ArrayList<Word> words);
    void createDeleteDialog();
    ArrayList<Long> getDeletedWords();
    void deleteWordsFromList();
    void createMoveToGroupDialog(ArrayList<Group> groups);
    ArrayList<Long> getMovedToGroupWords();
    void createMoveToTrainingDialog();
}
