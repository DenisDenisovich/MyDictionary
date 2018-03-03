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
import android.widget.Button;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.presenter.training.PresenterTrainingSecondToFirst;
import com.dictionary.my.mydictionary.presenter.training.impl.PresenterTrainingSecondToFirstImpl;
import com.dictionary.my.mydictionary.view.training.ViewTrainingSecondToFirst;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 01.02.2018.
 */

public class ViewTrainingSecondToFirstImpl extends Fragment implements ViewTrainingSecondToFirst{
    private View view;
    private final String KEY_ROWID = Content.COLUMN_ROWID;
    private final String KEY_TRANSLATE = Content.COLUMN_TRANSLATE;
    private long idOfSelectedTranslate;
    private int idOfSelectedView;
    PresenterTrainingSecondToFirst presenter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingSecondToFirstView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingSecondToFirstView: onCreate()" + this.hashCode());
        setRetainInstance(true);
        presenter = new PresenterTrainingSecondToFirstImpl(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingSecondToFirstView: onCreateView()" + this.hashCode());
        presenter.attach(this);
        view = inflater.inflate(R.layout.activity_main,null);
        if(savedInstanceState == null){
            Log.d("LOG_TAG", "savedInstanceState == null");
            presenter.init();
        }else{
            Log.d("LOG_TAG", "savedInstanceState != null");
            presenter.update();
        }
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG_TAG_C/D_Training", "TrainingSecondToFirstView: onDestroyView()" + this.hashCode());
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingSecondToFirstView: onDestroy()" + this.hashCode());
        presenter.destroy();
    }

    @Override
    public void setWord(String word) {
        String strWord = word;
       // ((TextView)view.findViewById(R.id.tvTSFWord)).setText(strWord);
    }

    @Override
    public void showProgress() {
      /*  ((TextView)view.findViewById(R.id.tvTSFWord)).setText("Processing");
        view.findViewById(R.id.tvTSFTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate5).setVisibility(View.GONE);*/
    }

    @Override
    public void hideProgress() {
       /* view.findViewById(R.id.tvTSFTranslate1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTSFTranslate2).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTSFTranslate3).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTSFTranslate4).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTSFTranslate5).setVisibility(View.VISIBLE);*/
    }

    @Override
    public void setTranslates(final ArrayList<Map<String, Object>> translations) {
       /* ((Button)view.findViewById(R.id.tvTSFTranslate1)).setText((String) translations.get(0).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTSFTranslate2)).setText((String) translations.get(1).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTSFTranslate3)).setText((String) translations.get(2).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTSFTranslate4)).setText((String) translations.get(3).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTSFTranslate5)).setText((String) translations.get(4).get(KEY_TRANSLATE));

        view.findViewById(R.id.tvTSFTranslate1).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTSFTranslate2).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTSFTranslate3).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTSFTranslate4).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTSFTranslate5).setBackgroundColor(Color.WHITE);

        view.findViewById(R.id.tvTSFTranslate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long) translations.get(0).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTSFTranslate1;
                presenter.translateIsSelected();
            }
        });
        view.findViewById(R.id.tvTSFTranslate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long) translations.get(1).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTSFTranslate2;
                presenter.translateIsSelected();
            }
        });
        view.findViewById(R.id.tvTSFTranslate3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long) translations.get(2).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTSFTranslate3;
                presenter.translateIsSelected();
            }
        });
        view.findViewById(R.id.tvTSFTranslate4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long) translations.get(3).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTSFTranslate4;
                presenter.translateIsSelected();
            }
        });
        view.findViewById(R.id.tvTSFTranslate5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long) translations.get(4).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTSFTranslate5;
                presenter.translateIsSelected();
            }
        });*/

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
     /*   ((TextView)view.findViewById(R.id.tvTSFWord)).setText(message);
        view.findViewById(R.id.tvTSFTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate5).setVisibility(View.GONE);*/
    }

    @Override
    public void setErrorMessage(String message) {
      /*  ((TextView)view.findViewById(R.id.tvTSFWord)).setText(message);
        view.findViewById(R.id.tvTSFTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTSFTranslate5).setVisibility(View.GONE);*/
    }



}
