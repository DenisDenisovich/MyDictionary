package com.dictionary.my.mydictionary.view.trainings.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.presenter.trainings.PresenterAllTrainings;
import com.dictionary.my.mydictionary.presenter.trainings.impl.PresenterAllTrainingsImpl;
import com.dictionary.my.mydictionary.view.dictionary.adapters.SpinnerAdapter;
import com.dictionary.my.mydictionary.view.trainings.ViewAllTrainings;

import org.w3c.dom.Text;

/**
 * This class is used for show screen of all trainings
 */

public class AllTrainingsFragment extends Fragment implements ViewAllTrainings {
    private final static String LOG_TAG = "Log_ViewAllTrainings";
    private PresenterAllTrainings presenter;
    private Spinner spinnerChoiceWords;
    private TextView tvEngRusCountOfWords;
    private TextView tvRusEngCountOfWords;
    private TextView tvConstructorCountOfWords;
    private TextView tvSprintCountOfWords;

    private static final String KEY_TRAINING_IS_CLICKED = "trainingIsClicked";
    private Boolean trainingIsClicked = false;

    public interface onTrainingSelectedListener{
        void showEngRusTraining();
        void showRusEngTraining();
        void showConstructorTraining();
        void showSprintTraining();
    }
    private onTrainingSelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach()      " + this.hashCode());
        try{
            mListener = (onTrainingSelectedListener)context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()      " + this.hashCode());
        presenter = new PresenterAllTrainingsImpl(getActivity().getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        // use this method, because fragment don't call onCreateView after closing Training Activity
        Log.d(LOG_TAG, "onStart()  " + this.hashCode());
        if(trainingIsClicked){
            presenter.init();
            trainingIsClicked = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()  " + this.hashCode());
        presenter.attach(this);
        View view = inflater.inflate(R.layout.fragment_all_trainings,null);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((TextView)view.findViewById(R.id.toolbarTitle)).setText("All Trainings");
        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        spinnerChoiceWords = view.findViewById(R.id.spinnerChoiceWords);
        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.all_trainings_spinner));
        spinnerChoiceWords.setAdapter(adapter);
        if(savedInstanceState == null){
            presenter.init();
        }else{
            presenter.update();
            trainingIsClicked = savedInstanceState.getBoolean(KEY_TRAINING_IS_CLICKED);
        }

        tvEngRusCountOfWords = view.findViewById(R.id.tvEngRusCountOfWords);
        tvRusEngCountOfWords = view.findViewById(R.id.tvRusEngCountOfWords);
        tvConstructorCountOfWords = view.findViewById(R.id.tvConstructorCountOfWords);
        tvSprintCountOfWords = view.findViewById(R.id.tvSprintCountOfWords);
        view.findViewById(R.id.cvEngRus).setOnClickListener(view1 -> {mListener.showEngRusTraining(); trainingIsClicked = true;});

        view.findViewById(R.id.cvRusEng).setOnClickListener(view12 -> {mListener.showRusEngTraining(); trainingIsClicked = true;});

        view.findViewById(R.id.cvConstructor).setOnClickListener(view13 -> {mListener.showConstructorTraining(); trainingIsClicked = true;});

        view.findViewById(R.id.cvSprint).setOnClickListener(view14 -> {mListener.showSprintTraining(); trainingIsClicked = true;});
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_TRAINING_IS_CLICKED, trainingIsClicked);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setERROR(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSpinnerItem(Integer item) {
        spinnerChoiceWords.setSelection(item);
        spinnerChoiceWords.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(item == 0){
                    //presenter.myWordsSelected();
                }else {
                    presenter.recentlyAddedSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void setEngRusCountOfWords(Integer count) {
        Log.d(LOG_TAG, "setEngRusCountOfWords()");
        tvEngRusCountOfWords.setText(String.valueOf(count));
    }

    @Override
    public void setRusEngCountOfWords(Integer count) {
        Log.d(LOG_TAG, "setRusEngCountOfWords()");
        tvRusEngCountOfWords.setText(String.valueOf(count));
    }

    @Override
    public void setConstructorCountOfWords(Integer count) {
        Log.d(LOG_TAG, "setConstructorCountOfWords()");
        tvConstructorCountOfWords.setText(String.valueOf(count));
    }

    @Override
    public void setSprintCountOfWords(Integer count) {
        Log.d(LOG_TAG, "setSprintCountOfWords()");
        tvSprintCountOfWords.setText(String.valueOf(count));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView() " + this.hashCode());
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()     " + this.hashCode());
        presenter.destroy();
    }
}
