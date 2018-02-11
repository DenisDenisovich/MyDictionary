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

public class TrainingConstructorView extends Fragment implements TrainingConstructor, HostToTrainingConstructorCommands {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingConstructorView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onCreate()" + this.hashCode());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onCreateView()" + this.hashCode());
        View view = inflater.inflate(R.layout.training_constructor_fragment,null);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onDestroyView()" + this.hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onDestroy()" + this.hashCode());
    }
}
