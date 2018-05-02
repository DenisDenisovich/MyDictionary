package com.dictionary.my.mydictionary.view.trainings.impl;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 24.04.2018.
 */

public class SprintTrainingActivity extends AppCompatActivity{
    private final static String LOG_TAG = "Log_SprintTraining";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_sprint_training);
        Log.d(LOG_TAG, "onCreate()");
    }
}
