package com.dictionary.my.mydictionary.view.dictionary.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.view.dictionary.adapters.DialogListAdapter;

import java.util.ArrayList;

/**
 * Created by luxso on 21.04.2018.
 */

public class MoveToTrainingDialog extends DialogFragment {
    private final String KEY_TRAINING_ID_LIST = "trainingsId";
    private int selectedItemId;
    private String selectedItemTitle;
    public static MoveToTrainingDialog newInstance(ArrayList<Long> list){
        final String KEY_TRAINING_ID_LIST = "trainingsId";
        long[] trainingId = new long[list.size()];
        for(int i = 0; i < list.size(); i++){
            trainingId[i] = list.get(i);
        }

        MoveToTrainingDialog d = new MoveToTrainingDialog();
        Bundle b = new Bundle();
        b.putLongArray(KEY_TRAINING_ID_LIST,trainingId);
        d.setArguments(b);
        return d;
    }

    public interface MoveToTrainingListener{
        void onMoveToGroupPositiveClick(Integer groupId, String groupTitle);
    }
    MoveToTrainingListener mListener;
    private boolean isActivity = false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MoveToTrainingListener) context;
            isActivity = true;
        }catch (ClassCastException e){
            isActivity = false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final long[] trainingId = getArguments().getLongArray(KEY_TRAINING_ID_LIST);
        final String[] trainingTitle = getResources().getStringArray(R.array.training_array);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_move_to_training,null);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
        ((TextView)view.findViewById(R.id.tvDialogMoveToTrainingHead)).setTypeface(typeface);

        ListView lv = view.findViewById(R.id.lvDialogMoveToTraining);
        DialogListAdapter adapter = new DialogListAdapter(getContext(),android.R.layout.simple_list_item_single_choice,trainingTitle,trainingId);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((adapterView, view1, i, l) -> {
            setItemId(i+1,adapter.getItemTitle(i));
            ((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        });
        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.move_to_training_positive_button), (dialogInterface, i) -> {
                    if (isActivity){
                        mListener.onMoveToGroupPositiveClick(selectedItemId,selectedItemTitle);
                    }else {
                        Intent data = new Intent();
                        data.putExtra(Content.COLUMN_ROWID, selectedItemId);
                        data.putExtra(Content.COLUMN_TRAININGS, selectedItemTitle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.move_to_training_negative_button), (dialogInterface, i) -> {
                    if (isActivity){
                        dismiss();
                    }else {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                    }
                });
        return builder.create();
    }

    private void setItemId(int l, String title){
        selectedItemId = l;
        selectedItemTitle = title;
    }

    @Override
    public void onStart() {
        super.onStart();
        //((AlertDialog)getDialog()).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }


}
