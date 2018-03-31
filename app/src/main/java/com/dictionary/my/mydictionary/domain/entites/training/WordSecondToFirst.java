package com.dictionary.my.mydictionary.domain.entites.training;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by luxso on 24.02.2018.
 */

public class WordSecondToFirst {
    private String word;
    private long wordId;
    private ArrayList<String> translations;
    private ArrayList<Long> translationsId;
    private Long rightTranslationId;
    private String rightTranslation;

    public WordSecondToFirst(){
        translations = new ArrayList<>();
        translationsId = new ArrayList<>();
    }

    public void setWord(long id, String word) {
        wordId = id;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public long getWordId() {
        return wordId;
    }

    public ArrayList<String> getTranslations() {
        return translations;
    }

    public void setNewTranslate(long id, String translate) {
        translations.add(translate);
        translationsId.add(id);
    }

    public ArrayList<Long> getTranslationsId() {
        return translationsId;
    }

    public void setRightTranslation(long id, String rightTranslation) {
        translations.add(rightTranslation);
        translationsId.add(id);
        rightTranslationId = id;
        this.rightTranslation = rightTranslation;

    }
    public void changeRightTranslationIndex(){
        Random rand = new Random();
        int index = rand.nextInt(translations.size());

        translations.remove(rightTranslation);
        translations.add(index,rightTranslation);

        translationsId.remove(rightTranslationId);
        translationsId.add(index, rightTranslationId);
    }
    public Long getRightTranslationId() {
        return rightTranslationId;
    }

}
