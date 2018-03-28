package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

    private final static String KEY_COUNT_WORDS = "CountWords";
    public static DeleteWordDialog newInstance(Integer wordsCount){
        final String KEY_COUNT_WORDS = "CountWords";
        DeleteWordDialog d = new DeleteWordDialog();
        Bundle arg = new Bundle();
        arg.putInt(KEY_COUNT_WORDS, wordsCount);
        d.setArguments(arg);
        return d;
    }

    public interface DeleteWordListener{
        public void onDeleteWordPositiveClick();
    }
    DeleteWordListener mListener;
    private boolean isActivity = false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DeleteWordListener) context;
            isActivity = true;
        }catch (ClassCastException e){
            isActivity = false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Integer countWords = getArguments().getInt(KEY_COUNT_WORDS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete,null);
        if(countWords == 1){
            ((TextView)view.findViewById(R.id.tvDialogDeleteMessage)).setText(getResources().getString(R.string.delete_dialog_message_one_word));
        }else{
            String message = getResources().getString(R.string.delete_dialog_message_couple_words);
            message = message.replace("this",String.valueOf(countWords));
            ((TextView)view.findViewById(R.id.tvDialogDeleteMessage)).setText(message);
        }
        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.delete_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isActivity){
                            mListener.onDeleteWordPositiveClick();
                        }else {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.delete_dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isActivity){
                            dismiss();
                        }else {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                        }
                    }
                });
        return builder.create();
    }
}
