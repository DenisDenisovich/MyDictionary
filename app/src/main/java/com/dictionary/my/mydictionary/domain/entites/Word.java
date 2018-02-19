package com.dictionary.my.mydictionary.domain.entites;

/**
 * Created by luxso on 18.02.2018.
 */

public class Word {

    private long id;
    private String word;
    private String translate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
