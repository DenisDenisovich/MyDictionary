package com.dictionary.my.mydictionary.domain.entites.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * This class is used in data layout of trainings for getting all necessary information about word
 */

public class WordForTraining extends Word {
    private ArrayList<String> alternative;

    public ArrayList<String> getAlternative() {
        return alternative;
    }

    public void setAlternative(ArrayList<String> alternative) {
        this.alternative = alternative;
    }
}
