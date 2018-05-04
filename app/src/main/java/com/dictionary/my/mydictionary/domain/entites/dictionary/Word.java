package com.dictionary.my.mydictionary.domain.entites.dictionary;

/**
 * this class use for list of words in AllWords class.
 * He contains minimum information about word
 */

public class Word {

    private String id;
    private String word;
    private String translate;
    private String sound;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

}
