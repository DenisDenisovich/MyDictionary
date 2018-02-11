package com.dictionary.my.mydictionary.view.training;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 01.02.2018.
 */

public class TrainingSprintView extends Fragment implements TrainingConstructor, HostToTrainingSprintCommands{
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingSprintView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onCreate()" + this.hashCode());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onCreateView()" + this.hashCode());
        View view = inflater.inflate(R.layout.training_sprint_fragment,null);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onDestroyView()" + this.hashCode());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onDestroy()" + this.hashCode());
    }
}
