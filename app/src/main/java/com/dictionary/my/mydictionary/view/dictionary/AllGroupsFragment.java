package com.dictionary.my.mydictionary.view.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

/**
 * Created by luxso on 04.03.2018.
 */

public interface AllGroupsFragment {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void createList(ArrayList<Group> words);
    void createNewGroupDialog();
    Group getNewGroup();
    void createDeleteDialog();
    ArrayList<Long> getDeletedGroups();
    void deleteGroupFromList();
    void createEditDialog();
    Group getEditedGroup();
    void editGroupInList();

}
