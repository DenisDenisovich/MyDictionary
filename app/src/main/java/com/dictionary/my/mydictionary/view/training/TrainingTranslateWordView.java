package com.dictionary.my.mydictionary.view.training;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 01.02.2018.
 */

public class TrainingTranslateWordView extends Fragment implements TrainingTranslateWord {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingTranslateWordView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingTranslateWordView: onCreate()" + this.hashCode());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingTranslateWordView: onCreateView()" + this.hashCode());
        View view = inflater.inflate(R.layout.training_translate_word_fragment,null);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG_TAG_C/D_Training", "TrainingTranslateWordView: onDestroyView()" + this.hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingTranslateWordView: onDestroy()" + this.hashCode());

    }
}
