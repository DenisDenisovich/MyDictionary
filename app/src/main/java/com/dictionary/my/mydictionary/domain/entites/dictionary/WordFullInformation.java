package com.dictionary.my.mydictionary.domain.entites.dictionary;

import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * this class use for add new word to dataBase.
 * He contains all information about word
 */

public class WordFullInformation {
    private String id;
    private String eng;
    private String rus;
    private String note;
    private String transcription;
    private String sound;
    private String partOfSpeech;
    private String previewImage;
    private String image;
    private String definition;
    private List<Example> examples = null;
    private ArrayList<String> alternative;
    private String date;
    private String groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public ArrayList<String> getAlternative() {
        return alternative;
    }

    public void setAlternative(ArrayList<String> alternative) {
        this.alternative = alternative;
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
