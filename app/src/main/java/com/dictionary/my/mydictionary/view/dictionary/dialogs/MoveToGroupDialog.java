package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

/**
 * Created by luxso on 01.11.2017.
 */

public class MoveToGroupDialog extends DialogFragment{
    private final String KEY_GROUPS_TITLE_LIST = "groupsTitle";
    private final String KEY_GROUPS_ID_LIST = "groupsId";
    private String selectedItemId;
    private String selectedItemTitle;
    public static MoveToGroupDialog newInstance(ArrayList<Group> list){
        final String KEY_GROUPS_TITLE_LIST = "groupsTitle";
        final String KEY_GROUPS_ID_LIST = "groupsId";
        String[] groupTitle = new String[list.size()];
        String[] groupId = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            groupTitle[i] = list.get(i).getTitle();
            groupId[i] = list.get(i).getId();
        }

        MoveToGroupDialog d = new MoveToGroupDialog();
        Bundle b = new Bundle();
        b.putStringArray(KEY_GROUPS_TITLE_LIST,groupTitle);
        b.putStringArray(KEY_GROUPS_ID_LIST,groupId);
        d.setArguments(b);
        return d;
    }

    public interface MoveToGroupListener{
        void onMoveToGroupPositiveClick(String groupId, String groupTitle);
    }
    MoveToGroupListener mListener;
    private boolean isActivity = false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MoveToGroupListener) context;
            isActivity = true;
        }catch (ClassCastException e){
            isActivity = false;
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] groupsId = getArguments().getStringArray(KEY_GROUPS_ID_LIST);
        final String[] groupsTitle = getArguments().getStringArray(KEY_GROUPS_TITLE_LIST);
        builder.setTitle(getResources().getString(R.string.move_to_group_dialog_header))
                .setSingleChoiceItems(getArguments().getStringArray(KEY_GROUPS_TITLE_LIST), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setItemId(groupsId[i], groupsTitle[i]);
                ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }).setNegativeButton(getResources().getString(R.string.move_to_group_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if(isActivity){
                   dismiss();
               }else {
                   getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
               }
            }
        }).setPositiveButton(getResources().getString(R.string.move_to_group_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(isActivity){
                    mListener.onMoveToGroupPositiveClick(selectedItemId, selectedItemTitle);
                }else {
                    Intent data = new Intent();
                    data.putExtra(Content.COLUMN_ROWID, selectedItemId);
                    data.putExtra(Content.COLUMN_TITLE, selectedItemTitle);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                }
            }
        });
        return builder.create();
    }

    private void setItemId(String l, String title){
        selectedItemId = l;
        selectedItemTitle = title;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }
}
