package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.entites.dictionary.Group;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryAllGroups;
import com.dictionary.my.mydictionary.data.storage.dictionary.DBAllGroups;
import com.dictionary.my.mydictionary.data.storage.dictionary.impl.DBAllGroupsImpl;

import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 11.03.2018.
 */

public class RepositoryAllGroupsImpl implements RepositoryAllGroups {
    private DBAllGroups dbAllGroups;
    public RepositoryAllGroupsImpl(Context context){
        dbAllGroups = new DBAllGroupsImpl(context);
    }
    @Override
    public Single<ArrayList<Group>> getListOfGroups() {
        return dbAllGroups.getListOfGroups();
    }

    @Override
    public void setNewGroup(Single<Group> observable) {
        dbAllGroups.setNewGroup(observable);
    }

    @Override
    public void deleteGroups(Single<ArrayList<Long>> observable) {
        dbAllGroups.deleteGroups(observable);
    }

    @Override
    public void editGroup(Single<Group> observable) {
        dbAllGroups.editGroup(observable);
    }

    @Override
    public void destroy() {
        dbAllGroups.destroy();
    }
}
