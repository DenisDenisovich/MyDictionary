package com.dictionary.my.mydictionary.view.dictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;

/**
 * Created by luxso on 29.09.2017.
 */

public class AddDictionaryDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_dictionary_dialog,null);
        final EditText etDictionary = view.findViewById(R.id.etNewDictionaryDialog);
        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent data = new Intent();
                        data.putExtra(Content.fromAllDictionaries[1], etDictionary.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,data);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
}
