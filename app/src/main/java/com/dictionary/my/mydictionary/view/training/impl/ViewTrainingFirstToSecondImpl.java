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
import com.dictionary.my.mydictionary.presenter.training.PresenterTrainingFirstToSecond;
import com.dictionary.my.mydictionary.presenter.training.impl.PresenterTrainingFirstToSecondImpl;
import com.dictionary.my.mydictionary.view.training.ViewTrainingFirstToSecond;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 23.02.2018.
 */

public class ViewTrainingFirstToSecondImpl extends Fragment implements ViewTrainingFirstToSecond {
    private View view;
    private final String KEY_ROWID = Content.COLUMN_ROWID;
    private final String KEY_TRANSLATE = Content.COLUMN_TRANSLATE;
    private long idOfSelectedTranslate;
    private int idOfSelectedView;
    PresenterTrainingFirstToSecond presenter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingFirstToSecondView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingFirstToSecondView: onCreate()" + this.hashCode());
        setRetainInstance(true);
        presenter = new PresenterTrainingFirstToSecondImpl(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingFirstToSecondView: onCreateView()" + this.hashCode());
        presenter.attach(this);
        view = inflater.inflate(R.layout.training_first_to_second_fragment,null);
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
        Log.d("LOG_TAG_C/D_Training", "TrainingFirstToSecondView: onDestroyView()" + this.hashCode());
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingFirstToSecondView: onDestroy()" + this.hashCode());
        presenter.destroy();
    }

    @Override
    public void setWord(String word) {
        String strWord = word;
        ((TextView)view.findViewById(R.id.tvTFSWord)).setText(strWord);
    }

    @Override
    public void showProgress() {
        ((TextView)view.findViewById(R.id.tvTFSWord)).setText("Processing");
        view.findViewById(R.id.tvTFSTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate5).setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        view.findViewById(R.id.tvTFSTranslate1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTFSTranslate2).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTFSTranslate3).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTFSTranslate4).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvTFSTranslate5).setVisibility(View.VISIBLE);
    }

    @Override
    public void setTranslates(final ArrayList<Map<String, Object>> translations) {
        ((Button)view.findViewById(R.id.tvTFSTranslate1)).setText((String)translations.get(0).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTFSTranslate2)).setText((String)translations.get(1).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTFSTranslate3)).setText((String)translations.get(2).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTFSTranslate4)).setText((String)translations.get(3).get(KEY_TRANSLATE));
        ((Button)view.findViewById(R.id.tvTFSTranslate5)).setText((String)translations.get(4).get(KEY_TRANSLATE));

        view.findViewById(R.id.tvTFSTranslate1).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTFSTranslate2).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTFSTranslate3).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTFSTranslate4).setBackgroundColor(Color.WHITE);
        view.findViewById(R.id.tvTFSTranslate5).setBackgroundColor(Color.WHITE);

        view.findViewById(R.id.tvTFSTranslate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translations.get(0).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTFSTranslate1;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTFSTranslate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translations.get(1).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTFSTranslate2;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTFSTranslate3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translations.get(2).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTFSTranslate3;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTFSTranslate4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translations.get(3).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTFSTranslate4;
                presenter.TranslateIsSelected();
            }
        });
        view.findViewById(R.id.tvTFSTranslate5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idOfSelectedTranslate = (Long)translations.get(4).get(KEY_ROWID);
                idOfSelectedView = R.id.tvTFSTranslate5;
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
        ((TextView)view.findViewById(R.id.tvTFSWord)).setText(message);
        view.findViewById(R.id.tvTFSTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate5).setVisibility(View.GONE);
    }

    @Override
    public void setErrorMessage(String message) {
        ((TextView)view.findViewById(R.id.tvTFSWord)).setText(message);
        view.findViewById(R.id.tvTFSTranslate1).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate2).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate3).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate4).setVisibility(View.GONE);
        view.findViewById(R.id.tvTFSTranslate5).setVisibility(View.GONE);
    }
}
