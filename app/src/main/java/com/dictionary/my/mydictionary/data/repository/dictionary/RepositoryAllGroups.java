package com.dictionary.my.mydictionary.data.repository.dictionary;

import com.dictionary.my.mydictionary.data.entites.dictionary.Group;

import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 11.03.2018.
 */

public interface RepositoryAllGroups {
    Single<ArrayList<Group>> getListOfGroups();
    void setNewGroup(Single<Group> observable);
    void deleteGroups(Single<ArrayList<Long>> observable);
    void editGroup(Single<Group> observable);
    void destroy();
}
