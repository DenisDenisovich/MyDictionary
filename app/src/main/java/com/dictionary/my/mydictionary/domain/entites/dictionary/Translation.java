package com.dictionary.my.mydictionary.domain.entites.dictionary;

/**
 * this class use for get translation by network from skyengapi
 */

public class Translation {
    // this objects sets in AddWordDialogs
    private String eng;
    private String date;
    private Long groupId;

    // this objects sets in CloudAllWords
    private String rus;
    private String sound;
    private String preview_image;
    private Integer meaningId;

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


    public Integer getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(Integer meaningId) {
        this.meaningId = meaningId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
