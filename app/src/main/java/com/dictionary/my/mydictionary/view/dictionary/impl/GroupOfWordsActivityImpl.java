package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;

import java.util.ArrayList;

/**
 * Created by luxso on 28.03.2018.
 */

public class GroupOfWordsActivityImpl extends AppCompatActivity implements ViewAllWords{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showERROR(String message) {

    }

    @Override
    public void createList(ArrayList<Word> words) {

    }

    @Override
    public void createDeleteDialog() {

    }

    @Override
    public ArrayList<Long> getDeletedWords() {
        return null;
    }

    @Override
    public void deleteWordsFromList() {

    }

    @Override
    public void createMoveToGroupDialog(ArrayList<Group> groups) {

    }

    @Override
    public ArrayList<Long> getMovedToGroupWords() {
        return null;
    }

    @Override
    public void createMoveToTrainingDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
