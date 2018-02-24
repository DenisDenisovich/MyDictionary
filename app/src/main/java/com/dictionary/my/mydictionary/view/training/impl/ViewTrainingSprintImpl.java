package com.dictionary.my.mydictionary.view.training.impl;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.presenter.training.PresenterTrainingSprint;
import com.dictionary.my.mydictionary.presenter.training.impl.PresenterTrainingSprintImpl;
import com.dictionary.my.mydictionary.view.training.HostToTrainingSprintCommands;
import com.dictionary.my.mydictionary.view.training.ViewTrainingSprint;

/**
 * Created by luxso on 01.02.2018.
 */

public class ViewTrainingSprintImpl extends Fragment implements HostToTrainingSprintCommands, ViewTrainingSprint {

    View view;
    PresenterTrainingSprint presenter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingSprintView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onCreate()" + this.hashCode());
        setRetainInstance(true);
        presenter = new PresenterTrainingSprintImpl(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onCreateView()" + this.hashCode());
        presenter.attach(this);
        view = inflater.inflate(R.layout.training_sprint_fragment,null);
        if(savedInstanceState == null){
            presenter.init();
        }else{
            presenter.update();
        }
        view.findViewById(R.id.btnTrainingSprintRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.rightButtonClick();
            }
        });
        view.findViewById(R.id.btnTrainingSprintWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.wrongButtonClick();
            }
        });
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
        presenter.destroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingSprintView: onDestroy()" + this.hashCode());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setWord(String word) {
        view.setBackgroundColor(Color.WHITE);
        ((TextView)view.findViewById(R.id.tvTrainingSprintWord)).setText(word);
    }

    @Override
    public void setTranslate(String translate) {
        ((TextView)view.findViewById(R.id.tvTrainingSprintTranslate)).setText(translate);
    }

    @Override
    public void setTime(String time) {
        ((TextView)view.findViewById(R.id.tvTrainingSprintTime)).setText(time);
    }

    @Override
    public void setPositiveMessage() {
        view.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void setNegativeMessage() {
        view.setBackgroundColor(Color.RED);
    }

    @Override
    public void setResultMessage(String message) {
        view.setBackgroundColor(Color.WHITE);
        ((TextView)view.findViewById(R.id.tvTrainingSprintWord)).setText(message);
        ((TextView)view.findViewById(R.id.tvTrainingSprintTime)).setText("");
        ((TextView)view.findViewById(R.id.tvTrainingSprintTranslate)).setText("");
        view.findViewById(R.id.btnTrainingSprintRight).setVisibility(View.GONE);
        view.findViewById(R.id.btnTrainingSprintWrong).setVisibility(View.GONE);
    }

    @Override
    public void setErrorMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }
}
