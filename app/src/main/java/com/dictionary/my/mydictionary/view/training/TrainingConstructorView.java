package com.dictionary.my.mydictionary.view.training;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.presenter.trainings.TrainingConstructorPresenter;
import com.dictionary.my.mydictionary.presenter.trainings.TrainingConstructorPresenterImpl;

import java.util.ArrayList;

/**
 * Created by luxso on 01.02.2018.
 */

public class TrainingConstructorView extends Fragment implements TrainingConstructor, HostToTrainingConstructorCommands {
    private TrainingConstructorPresenter presenter;
    View view;
    private LinearLayout linLayout;
    private TextView tvWord;
    private TextView tvTranslate;
    private String clickedSymbol;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LOG_TAG", "TrainingConstructorView: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onCreate()" + this.hashCode());
        presenter = new TrainingConstructorPresenterImpl(getActivity().getApplicationContext());
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onCreateView()" + this.hashCode());
        presenter.attach(this);
        view = inflater.inflate(R.layout.training_constructor_fragment,null);
        linLayout = view.findViewById(R.id.llContainer);
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
        presenter.detach();
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onDestroyView()" + this.hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorView: onDestroy()" + this.hashCode());
    }

    @Override
    public void setWord(String word) {
        Log.d("LOG_TAG", "ConstView setWord()");
        linLayout.removeAllViewsInLayout();
        // создаем TextView для word
        ((TextView)view.findViewById(R.id.tvTrainingConstructorWord)).setText(word);
        view/*.findViewById(R.id.tvTrainingConstructorWord)*/.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void setSymbols(final ArrayList<String> translate) {
        Log.d("LOG_TAG", "ConstView setSymbols()");
        LinearLayout subLinearLayout = new LinearLayout(getActivity());
        int symbolCount = translate.size();
        for(int i = 0; i < symbolCount; i++){
            if(i % 5 == 0){
                subLinearLayout = new LinearLayout(getActivity());
                subLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                subLinearLayout.setLayoutParams(linLayoutParam);
                subLinearLayout.setGravity(Gravity.CENTER);
                linLayout.addView(subLinearLayout);
            }
            final Button btnSymbol = new Button(getActivity());
            LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(100,ViewGroup.LayoutParams.WRAP_CONTENT);
            lpView.setMargins(10,10,10,10);
            btnSymbol.setLayoutParams(lpView);
            btnSymbol.setText(translate.get(i));
            subLinearLayout.addView(btnSymbol);
            btnSymbol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickedSymbol = btnSymbol.getText().toString();
                    presenter.symbolClick();
                    btnSymbol.setVisibility(View.GONE);
                }
            });
        }

    }

    public void setCurrentTranslate(String currentTranslate) {
        Log.d("LOG_TAG", "ConstView setCurrentTranslate()");
        ((TextView)view.findViewById(R.id.tvTrainingConstructorTranslate)).setText(currentTranslate);
    }

    public String getClickSymbol() {
        Log.d("LOG_TAG", "ConstView getClickSymbol()");
        return clickedSymbol;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setPositiveMessage() {
        view/*.findViewById(R.id.tvTrainingConstructorWord)*/.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void setNegativeMessage() {
        view/*.findViewById(R.id.tvTrainingConstructorWord)*/.setBackgroundColor(Color.RED);
    }

    @Override
    public void setResultMessage(String message) {
        linLayout.removeAllViews();
        view/*.findViewById(R.id.tvTrainingConstructorWord)*/.setBackgroundColor(Color.WHITE);
        ((TextView)view.findViewById(R.id.tvTrainingConstructorWord)).setText(message);
        ((TextView)view.findViewById(R.id.tvTrainingConstructorTranslate)).setText("");
    }

    @Override
    public void setErrorMessage(String message) {
        Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
    }
}
