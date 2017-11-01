package com.dictionary.my.mydictionary.view.dictionary;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by luxso on 01.11.2017.
 */

public class MoveWordDialog  extends DialogFragment{
    private final String KEY_ALL_DICTIONARIES_LIST = "allDictionaries";
    public static MoveWordDialog newInstance(long ... list){
        final String KEY_ALL_DICTIONARIES_LIST = "allDictionaries";
        MoveWordDialog d = new MoveWordDialog();
        Bundle b = new Bundle();
        b.putLongArray(KEY_ALL_DICTIONARIES_LIST,list);
        d.setArguments(b);
        return d;
    }


}
