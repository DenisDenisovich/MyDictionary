package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 26.01.2018.
 */

public class DeleteWordDialog extends DialogFragment {

    final String KEY_COUNT_WORDS = "CountWords";
    public static DeleteWordDialog newInstance(Integer wordsCount){
        final String KEY_COUNT_WORDS = "CountWords";
        DeleteWordDialog d = new DeleteWordDialog();
        Bundle arg = new Bundle();
        arg.putInt(KEY_COUNT_WORDS, wordsCount);
        d.setArguments(arg);
        return d;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Integer countWords = getArguments().getInt(KEY_COUNT_WORDS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_dialog,null);
        ((TextView)view.findViewById(R.id.tvDialogDeleteHead)).setText(getResources().getString(R.string.dialog_delete_word_TextView_head));
        if(countWords == 1){
            ((TextView)view.findViewById(R.id.tvDialogDeleteMessage)).setText(getResources().getString(R.string.dialog_delete_one_word_message));
        }else{
            ((TextView)view.findViewById(R.id.tvDialogDeleteMessage)).setText(getResources().getString(R.string.dialog_delete_words_message));
        }
        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.dialog_delete_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,null);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_delete_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,null);
                    }
                });
        return builder.create();
    }
}
