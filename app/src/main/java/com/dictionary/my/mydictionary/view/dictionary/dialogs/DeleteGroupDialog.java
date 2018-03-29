package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 27.03.2018.
 */

public class DeleteGroupDialog extends DialogFragment{
    private final static String KEY_COUNT_GROUPS = "CountGroups";
    public static DeleteGroupDialog newInstance(Integer wordsCount){
        final String KEY_COUNT_GROUPS = "CountGroups";
        DeleteGroupDialog d = new DeleteGroupDialog();
        Bundle arg = new Bundle();
        arg.putInt(KEY_COUNT_GROUPS, wordsCount);
        d.setArguments(arg);
        return d;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Integer countGroups = getArguments().getInt(KEY_COUNT_GROUPS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete,null);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
        ((TextView) view.findViewById(R.id.tvDialogDeleteMessage)).setTypeface(typeface);
        ((TextView) view.findViewById(R.id.tvDialogDeleteHead)).setTypeface(typeface);
        if(countGroups == 1){
            ((TextView)view.findViewById(R.id.tvDialogDeleteMessage)).setText(getResources().getString(R.string.delete_dialog_message_one_group));
        }else{
            String message = getResources().getString(R.string.delete_dialog_message_couple_groups);
            message = message.replace("this",String.valueOf(countGroups));
            ((TextView)view.findViewById(R.id.tvDialogDeleteMessage)).setText(message);

        }
        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.delete_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,null);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.delete_dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,null);
                    }
                });
        return builder.create();
    }
}
