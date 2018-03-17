package com.dictionary.my.mydictionary.data.repository.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface RepositoryAllGroups {
    Single<ArrayList<Group>> getListOfGroups();
    Completable setNewGroup(Group group);
    Completable deleteGroups(ArrayList<Long> delList);
    Completable editGroup(Group editGroup);
    void destroy();
}
