package com.dictionary.my.mydictionary.view.training;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by luxso on 23.09.2017.
 */

public class EngRusTrainingView extends Fragment implements View.OnClickListener{
    String[] word = {"Training","Word","Man","Exception","DictionaryView","Table","Apple","Up","Down","World","Note"};
    String[] translate = {"Тренировка","Слово","Человек","Исключение","Словарь","Стол","Яблоко","Верх","Низ","Мир","Тетрадь"};
    int rightTranslateBtnIndex;
    TextView tvWord;
    Button btnTranslate1, btnTranslate2, btnTranslate3, btnTranslate4, btnTranslate5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.training_fragment, null);
        tvWord = (TextView) view.findViewById(R.id.trainingWord);
        btnTranslate1 = (Button) view.findViewById(R.id.btnTrainingTranslate1);
        btnTranslate1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate1.setOnClickListener(this);

        btnTranslate2 = (Button) view.findViewById(R.id.btnTrainingTranslate2);
        btnTranslate2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate2.setOnClickListener(this);

        btnTranslate3 = (Button) view.findViewById(R.id.btnTrainingTranslate3);
        btnTranslate3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate3.setOnClickListener(this);

        btnTranslate4 = (Button) view.findViewById(R.id.btnTrainingTranslate4);
        btnTranslate4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate4.setOnClickListener(this);

        btnTranslate5 = (Button) view.findViewById(R.id.btnTrainingTranslate5);
        btnTranslate5.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate5.setOnClickListener(this);
        createTask myTask = new createTask();
        myTask.execute();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTrainingTranslate1:
                if(rightTranslateBtnIndex == 0){
                    btnTranslate1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonRight));
                    Toast.makeText(getActivity(), "Right",Toast.LENGTH_SHORT);
                }else{
                    btnTranslate1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonWrong));
                    Toast.makeText(getActivity(), "Wrong",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btnTrainingTranslate2:
                if(rightTranslateBtnIndex == 1){
                    btnTranslate2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonRight));
                    Toast.makeText(getActivity(), "Right",Toast.LENGTH_SHORT);
                }else{
                    btnTranslate2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonWrong));
                    Toast.makeText(getActivity(), "Wrong",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btnTrainingTranslate3:
                if(rightTranslateBtnIndex == 2){
                    btnTranslate3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonRight));
                    Toast.makeText(getActivity(), "Right",Toast.LENGTH_SHORT);
                }else{
                    btnTranslate3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonWrong));
                    Toast.makeText(getActivity(), "Wrong",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btnTrainingTranslate4:
                if(rightTranslateBtnIndex == 3){
                    btnTranslate4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonRight));
                    Toast.makeText(getActivity(), "Right",Toast.LENGTH_SHORT);
                }else{
                    btnTranslate4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonWrong));
                    Toast.makeText(getActivity(), "Wrong",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btnTrainingTranslate5:
                if(rightTranslateBtnIndex == 4){
                    btnTranslate5.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonRight));
                    Toast.makeText(getActivity(), "Right",Toast.LENGTH_SHORT);
                }else{
                    btnTranslate5.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButtonWrong));
                    Toast.makeText(getActivity(), "Wrong",Toast.LENGTH_SHORT);
                }
                break;
            default:
                Toast.makeText(getActivity(), "NO",Toast.LENGTH_SHORT);
                break;
        }


        createTask myTask = new createTask();
        myTask.execute();
    }

   /* private void createTask(){
        // ATTENTION SHIT CODE
        Random rand = new Random();
        int wordIndex;
        int transIndex[] = new int[5];

        wordIndex = rand.nextInt(word.length);
        // генерируем случайную последовательнось 5 индексов, каждый из которых уникальный
        for(int i = 0; i < transIndex.length; i++){
            transIndex[i] = rand.nextInt(translate.length);
            for(int j = i-1; j >= 0; j--){
                if(transIndex[i] == transIndex[j]){
                    i--;
                    break;
                }
            }
        }
        // задаем случайную позицию правильному ответу
        rightTranslateBtnIndex = rand.nextInt(4);
        switch(rightTranslateBtnIndex){
            case 0:
                transIndex[0] = wordIndex;
                break;
            case 1:
                transIndex[1] = wordIndex;
                break;
            case 2:
                transIndex[2] = wordIndex;
                break;
            case 3:
                transIndex[3] = wordIndex;
                break;
            case 4:
                transIndex[4] = wordIndex;
                break;
        }
        tvWord.setText(word[wordIndex]);
        btnTranslate1.setText(translate[transIndex[0]]);
        btnTranslate2.setText(translate[transIndex[1]]);
        btnTranslate3.setText(translate[transIndex[2]]);
        btnTranslate4.setText(translate[transIndex[3]]);
        btnTranslate5.setText(translate[transIndex[4]]);
        btnTranslate1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        btnTranslate5.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
    }*/

    private class createTask extends AsyncTask<Void,Void,Integer[]>{


        @Override
        protected Integer[] doInBackground(Void... voids) {
            // ATTENTION SHIT CODE
            Random rand = new Random();
            int wordIndex;
            int transIndex[] = new int[5];

            wordIndex = rand.nextInt(word.length);
            // генерируем случайную последовательнось 5 индексов, каждый из которых уникальный
            for(int i = 0; i < transIndex.length; i++){
                transIndex[i] = rand.nextInt(translate.length);
                for(int j = i-1; j >= 0; j--){
                    if(transIndex[i] == transIndex[j]){
                        i--;
                        break;
                    }
                }
            }
            // задаем случайную позицию правильному ответу
            rightTranslateBtnIndex = rand.nextInt(4);
            switch(rightTranslateBtnIndex){
                case 0:
                    transIndex[0] = wordIndex;
                    break;
                case 1:
                    transIndex[1] = wordIndex;
                    break;
                case 2:
                    transIndex[2] = wordIndex;
                    break;
                case 3:
                    transIndex[3] = wordIndex;
                    break;
                case 4:
                    transIndex[4] = wordIndex;
                    break;
            }
            Integer[] result = new Integer[6];
            result[0] = wordIndex;
            result[1] = transIndex[0];
            result[2] = transIndex[1];
            result[3] = transIndex[2];
            result[4] = transIndex[3];
            result[5] = transIndex[4];


            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer[] result) {
            tvWord.setText(word[result[0]]);
            btnTranslate1.setText(translate[result[1]]);
            btnTranslate2.setText(translate[result[2]]);
            btnTranslate3.setText(translate[result[3]]);
            btnTranslate4.setText(translate[result[4]]);
            btnTranslate5.setText(translate[result[5]]);
            btnTranslate1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
            btnTranslate2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
            btnTranslate3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
            btnTranslate4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
            btnTranslate5.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorTrainingTranslateButton));
        }
    }
}

