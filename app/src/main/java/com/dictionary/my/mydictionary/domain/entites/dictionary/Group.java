package com.dictionary.my.mydictionary.domain.entites.dictionary;

/**
 * this class use for list of groups in AllGroups class.
 */

public class Group {
    private long id;
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
