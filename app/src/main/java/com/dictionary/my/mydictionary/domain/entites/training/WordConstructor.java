package com.dictionary.my.mydictionary.domain.entites.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 18.02.2018.
 */

public class WordConstructor extends Word {
    private ArrayList<String> charsOfTranlate;

    public ArrayList<String> getCharsOfTranlate() {
        return charsOfTranlate;
    }

    public void setCharsOfTranlate(ArrayList<String> charsOfTranlate) {
        this.charsOfTranlate = charsOfTranlate;
    }
}
