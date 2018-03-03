package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;

/**
 * Created by luxso on 01.11.2017.
 */

public class EditWordDialog extends DialogFragment{
    private final String KEY_OLD_TITLE = "oldTranslate";

    public static EditWordDialog newInstance(String oldTitle){
        final String KEY_OLD_TITLE = "oldTranslate";
        EditWordDialog d = new EditWordDialog();
        Bundle arg = new Bundle();
        arg.putString(KEY_OLD_TITLE,oldTitle);
        d.setArguments(arg);
        return d;
    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_word_dialog,null);
        TextView tvWordOldTranslate = view.findViewById(R.id.tvEditWordDialogOldTranslate);
        tvWordOldTranslate.setText(getArguments().getString(KEY_OLD_TITLE));
        final EditText etWordNewTranslate = view.findViewById(R.id.etEditWordDialogNewTranslate);
        etWordNewTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()) {
                    ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.dialog_edit_dictionary_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent data = new Intent();
                        data.putExtra(Content.fromDictionary[2], etWordNewTranslate.getText().toString().trim().replaceAll("\\s{2,}", " "));
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,data);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_edit_dictionary_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,null);
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }*/
}


