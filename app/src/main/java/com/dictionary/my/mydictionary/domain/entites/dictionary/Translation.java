package com.dictionary.my.mydictionary.domain.entites.dictionary;

/**
 * this class use for get translation by network from skyengapi
 */

public class Translation {
    // this objects sets in AddWordDialogs
    private String eng;
    private String date;
    private String groupId;

    // this objects sets in CloudAllWords
    private String rus;
    private String sound;
    private String preview_image;
    private String meaningId;

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getRus() {
        return rus;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }


    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }


    public String getPreview_image() {
        return preview_image;
    }

    public void setPreview_image(String preview_image) {
        this.preview_image = preview_image;
    }


    public String getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(String meaningId) {
        this.meaningId = meaningId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
