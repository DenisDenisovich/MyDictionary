package com.dictionary.my.mydictionary.data.exception;


/**
 * This exception need to detecting Exception in SkyEngMeaning
 */

public class SkyEngMeaningException extends Exception {
    public SkyEngMeaningException(){

    }
    public SkyEngMeaningException(Throwable e){
        initCause(e);
    }
}
