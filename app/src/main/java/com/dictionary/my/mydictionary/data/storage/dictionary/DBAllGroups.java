package com.dictionary.my.mydictionary.data.storage.dictionary;

import com.dictionary.my.mydictionary.data.entites.dictionary.Group;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by luxso on 30.10.2017.
 */

public interface DBAllGroups {
    Single<ArrayList<Group>> getListOfGroups();
    void setNewGroup(Single<Group> observable);
    void deleteGroups(Single<ArrayList<Long>> observable);
    void editGroup(Single<Group> observable);
    void destroy();
}
