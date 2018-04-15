package com.dictionary.my.mydictionary.data.exception;


/**
 * This exception need to detecting Exception in SkyEngWord
 */

public class TranslationIsNotFoundException extends Exception {
    public TranslationIsNotFoundException(String word){
        super("SkyEngApi can't find translation for word: " + word);
    }
}
