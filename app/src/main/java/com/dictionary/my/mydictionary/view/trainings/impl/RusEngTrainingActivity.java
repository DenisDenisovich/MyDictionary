package com.dictionary.my.mydictionary.view.trainings.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 24.04.2018.
 */

public class RusEngTrainingActivity extends AppCompatActivity {
    private final static String LOG_TAG = "Log_RusEngTraining";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rus_eng_training);
        Log.d(LOG_TAG, "onCreate()");

    }
}
