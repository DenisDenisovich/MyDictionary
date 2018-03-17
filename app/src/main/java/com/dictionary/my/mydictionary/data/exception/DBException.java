package com.dictionary.my.mydictionary.data.exception;

import android.database.sqlite.SQLiteException;


/**
 * This exception need to detecting IOException in DataBase
 */

public class DBException extends Exception{
    public DBException(){

    }
    public DBException(String message){
        super(message);
    }
}
