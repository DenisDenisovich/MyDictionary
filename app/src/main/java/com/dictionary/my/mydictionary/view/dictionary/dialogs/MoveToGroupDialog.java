package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 01.11.2017.
 */

public class MoveToGroupDialog extends DialogFragment{
    private final String KEY_ALL_DICTIONARIES_TITLE_LIST = "groupsTitle";
    private final String KEY_ALL_DICTIONARIES_ID_LIST = "groupsId";
    private long selectedItemId;
    public static MoveToGroupDialog newInstance(ArrayList<Group> list){
        final String KEY_ALL_DICTIONARIES_TITLE_LIST = "groupsTitle";
        final String KEY_ALL_DICTIONARIES_ID_LIST = "groupsId";
        String[] groupTitle = new String[list.size()];
        long[] groupId = new long[list.size()];
        for(int i = 0; i < list.size(); i++){
            groupTitle[i] = list.get(i).getTitle();
            groupId[i] = list.get(i).getId();
        }

        MoveToGroupDialog d = new MoveToGroupDialog();
        Bundle b = new Bundle();
        b.putStringArray(KEY_ALL_DICTIONARIES_TITLE_LIST,groupTitle);
        b.putLongArray(KEY_ALL_DICTIONARIES_ID_LIST,groupId);
        d.setArguments(b);
        return d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final long[] dictId = getArguments().getLongArray(KEY_ALL_DICTIONARIES_ID_LIST);
        builder.setTitle(getResources().getString(R.string.move_to_group_dialog_header))
                .setSingleChoiceItems(getArguments().getStringArray(KEY_ALL_DICTIONARIES_TITLE_LIST), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setItemId(dictId[i]);
                ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }).setNegativeButton(getResources().getString(R.string.move_to_group_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,null);
            }
        }).setPositiveButton(getResources().getString(R.string.move_to_group_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent data = new Intent();
                data.putExtra(Content.COLUMN_ROWID, selectedItemId);
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
