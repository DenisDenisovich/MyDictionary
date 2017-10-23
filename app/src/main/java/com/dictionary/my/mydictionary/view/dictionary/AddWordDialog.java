package com.dictionary.my.mydictionary.view.dictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 24.09.2017.
 */

public class AddWordDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_word_dialog,null);
        final EditText etWord = view.findViewById(R.id.newWordDialog);
        final EditText etTranslate = view.findViewById(R.id.newTranslateDialog);
        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent data = new Intent();
                        data.putExtra("word", etWord.getText().toString());
                        data.putExtra("translate", etTranslate.getText().toString());
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
