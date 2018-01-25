package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 01.11.2017.
 */

public class MoveWordDialog  extends DialogFragment{
    private final String KEY_ALL_DICTIONARIES_TITLE_LIST = "allDictionariesTitle";
    private final String KEY_ALL_DICTIONARIES_ID_LIST = "allDictionariesId";
    private long selectedItemId;
    public static MoveWordDialog newInstance(ArrayList<Map<String,Object>> list){
        final String KEY_ALL_DICTIONARIES_TITLE_LIST = "allDictionariesTitle";
        final String KEY_ALL_DICTIONARIES_ID_LIST = "allDictionariesId";
        String[] dictTitle = new String[list.size()];
        long[] dictId = new long[list.size()];
        for(int i = 0; i < list.size(); i++){
            dictTitle[i] = (String)list.get(i).get(Content.fromAllDictionaries[1]);
            dictId[i] = (Long)list.get(i).get(Content.fromAllDictionaries[0]);
            Log.d("LOG_TAG","inDialog " + dictId[i] + " " + dictTitle[i]);
        }

        MoveWordDialog d = new MoveWordDialog();
        Bundle b = new Bundle();
        b.putStringArray(KEY_ALL_DICTIONARIES_TITLE_LIST,dictTitle);
        b.putLongArray(KEY_ALL_DICTIONARIES_ID_LIST,dictId);
        d.setArguments(b);
        return d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final long[] dictId = getArguments().getLongArray(KEY_ALL_DICTIONARIES_ID_LIST);
        builder.setTitle(getResources().getString(R.string.dialog_move_word_TextView_head))
                .setSingleChoiceItems(getArguments().getStringArray(KEY_ALL_DICTIONARIES_TITLE_LIST), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setItemId(dictId[i]);
                ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }).setNegativeButton(getResources().getString(R.string.dialog_move_word_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,null);
            }
        }).setPositiveButton(getResources().getString(R.string.dialog_move_word_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent data = new Intent();
                data.putExtra(Content.fromAllDictionaries[0], selectedItemId);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,data);
            }
        });
        return builder.create();
    }

    private void setItemId(long l){
        selectedItemId = l;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }
}
