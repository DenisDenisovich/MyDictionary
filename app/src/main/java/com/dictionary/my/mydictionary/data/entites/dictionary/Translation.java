package com.dictionary.my.mydictionary.data.entites.dictionary;

import com.dictionary.my.mydictionary.data.entites.skyengapi.meaning.Example;

import java.util.List;

/**
 * this class use for get translation by network from skyengapi
 */

public class Translation {
    private String text;
    private String note;
    private String transcription;
    private String sound;
    private String partOfSpeech;
    private String preview_image;
    private String image;
    private String definition;
    private List<Example> examples = null;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getPreview_image() {
        return preview_image;
    }

    public void setPreview_image(String preview_image) {
        this.preview_image = preview_image;
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
}
