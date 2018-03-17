package com.dictionary.my.mydictionary.data.db.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 30.10.2017.
 */

public interface DBAllGroups {
    ArrayList<Group> getListOfGroups() throws Exception;
    void setNewGroup(Group group) throws Exception;
    void deleteGroups(ArrayList<Long> delList) throws Exception;
    void editGroup(Group editGroup)throws Exception;
    void destroy();
}
