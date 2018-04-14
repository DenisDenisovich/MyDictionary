package com.dictionary.my.mydictionary.data.db.dictionary;

import android.database.sqlite.SQLiteException;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 30.10.2017.
 */

public interface DBGroups {
    ArrayList<Group> getListOfGroups();
    void setNewGroup(Group group);
    void deleteGroups(ArrayList<Long> delList);
    void editGroup(Group editGroup);
    void destroy();
}
