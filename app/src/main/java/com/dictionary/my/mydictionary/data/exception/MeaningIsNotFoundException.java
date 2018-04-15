package com.dictionary.my.mydictionary.data.exception;


/**
 * This exception need to detecting Exception in SkyEngMeaning
 */

public class MeaningIsNotFoundException extends Exception {
    public MeaningIsNotFoundException(String word, String meaningId){
        super("SkyEngApi can't find meaning for {word: " + word + "; meaning: " + meaningId + "}");
    }
}
