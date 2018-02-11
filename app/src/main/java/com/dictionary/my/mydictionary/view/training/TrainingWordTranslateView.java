package com.dictionary.my.mydictionary.view.training;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.presenter.trainings.TrainingWordTranslatePresenter;
import com.dictionary.my.mydictionary.presenter.trainings.TrainingWordTranslatePresenterImpl;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by luxso on 01.02.2018.
 */

public class TrainingWordTranslateView extends Fragment implements TrainingWordTranslate, HostToTrainingWordTranslateCommands{
    private View view;
    private String givenWord;
    private String choiceTranslatingWords;
    private Map<String,Object> word;
    private ArrayList<Map<String,Object>> translate;
    private long idOfSelectedTranslate;
    private int idOfSelectedView;
    TrainingWordTranslatePresenter presenter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingWordTranslateView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingWordTranslateView: onCreate()" + this.hashCode());
        String[] languagesMode = new String[2];
        languagesMode[0] = givenWord;
        languagesMode[1] = choiceTranslatingWords;
        presenter = new TrainingWordTranslatePresenterImpl(getActivity().getApplicationContext(),languagesMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingWordTranslateView: onCreateView()" + this.hashCode());
        presenter.attach(this);
        view = inflater.inflate(R.layout.training_word_translate_fragment,null);
        if(savedInstanceState == null){
            presenter.init();
        }else{
            presenter.update();
        }
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG_TAG_C/D_Training", "TrainingWordTranslateView: onDestroyView()" + this.hashCode());
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingWordTranslateView: onDestroy()" + this.hashCode());
        presenter.destroy();
    }

    @Override
    public void setBaseToLearningTrainingMode() {
        givenWord = Content.COLUMN_WORD;
        choiceTranslatingWords = Content.COLUMN_TRANSLATE;
    }

    @Override
    public void setLearningToBaseTrainingMode() {
        givenWord = Content.COLUMN_TRANSLATE;
        choiceTranslatingWords = Content.COLUMN_WORD;
    }

    @Override
    public void setWord(Map<String, Object> word) {
        this.word = word;
        ((TextView)view.findViewById(R.id.tvTWTWord)).setText((String)word.get(givenWord));
    }

    @Override
    public void showProgress() {
        ((TextView)view.findViewById(R.id.tvTWTWord)).setText("Processing");
        view.findViewById(R.id.tvTWTTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate5).setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        view.findViewById(R.id.tvTWTTranslate1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTWTTranslate2).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTWTTranslate3).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTWTTranslate4).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTWTTranslate5).setVisibility(View.VISIBLE);
    }

    @Override
    public void setTranslates(final ArrayList<Map<String, Object>> translates) {
        this.translate = translates;
        ((Button)view.findViewById(R.id.tvTWTTranslate1)).setText((String)translates.get(0).get(choiceTranslatingWords));
        ((Button)view.findViewById(R.id.tvTWTTranslate1)).setBackgroundColor(Color.WHITE);
        ((Button)view.findViewById(R.id.tvTWTTranslate2)).setText((String)translates.get(1).get(choiceTranslatingWords));
        ((Button)view.findViewById(R.id.tvTWTTranslate2)).setBackgroundColor(Color.WHITE);
        ((Button)view.findViewById(R.id.tvTWTTranslate3)).setText((String)translates.get(2).get(choiceTranslatingWords));
        ((Button)view.findViewById(R.id.tvTWTTranslate3)).setBackgroundColor(Color.WHITE);
        ((Button)view.findViewById(R.id.tvTWTTranslate4)).setText((String)translates.get(3).get(choiceTranslatingWords));
        ((Button)view.findViewById(R.id.tvTWTTranslate4)).setBackgroundColor(Color.WHITE);
        ((Button)view.findViewById(R.id.tvTWTTranslate5)).setText((String)translates.get(4).get(choiceTranslatingWords));
        ((Button)view.findViewById(R.id.tvTWTTranslate5)).setBackgroundColor(Color.WHITE);

        view.findViewById(R.id.tvTWTTranslate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translates.get(0).get(Content.COLUMN_ROWID);
                idOfSelectedView = R.id.tvTWTTranslate1;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTWTTranslate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translates.get(1).get(Content.COLUMN_ROWID);
                idOfSelectedView = R.id.tvTWTTranslate2;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTWTTranslate3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translates.get(2).get(Content.COLUMN_ROWID);
                idOfSelectedView = R.id.tvTWTTranslate3;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTWTTranslate4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translates.get(3).get(Content.COLUMN_ROWID);
                idOfSelectedView = R.id.tvTWTTranslate4;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTWTTranslate5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translates.get(4).get(Content.COLUMN_ROWID);
                idOfSelectedView = R.id.tvTWTTranslate5;
                presenter.TranslateIsSelected();
            }
        });

    }


    @Override
    public long getSelectedTranslate() {
        return idOfSelectedTranslate;
    }

    @Override
    public void setPositiveAnswer() {
        view.findViewById(idOfSelectedView).setBackgroundColor(Color.BLUE);
    }

    @Override
    public void setNegativeAnswer() {
        view.findViewById(idOfSelectedView).setBackgroundColor(Color.RED);
    }

    @Override
    public void setResultMessage(String message) {
        ((TextView)view.findViewById(R.id.tvTWTWord)).setText(message);
        view.findViewById(R.id.tvTWTTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTWTTranslate5).setVisibility(View.GONE);
    }

    @Override
    public void setErrorMessage(String message) {

    }



}
