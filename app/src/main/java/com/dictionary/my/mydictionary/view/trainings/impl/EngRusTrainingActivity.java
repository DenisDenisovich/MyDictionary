package com.dictionary.my.mydictionary.view.trainings.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 24.04.2018.
 */

public class EngRusTrainingActivity extends AppCompatActivity {
    private final static String LOG_TAG = "Log_EngRusTraining";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_rus_training);
        Log.d(LOG_TAG, "onCreate()");

    }
}
