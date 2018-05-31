package com.dictionary.my.mydictionary.view.statistic;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 31.05.2018.
 */

public interface ViewStatistic {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void showCountOfWords(Integer count);
    void showPartOfSpeechStatistic(Map<String, Integer> partOfSpeech);
    void showWordsInMonthStatistic(ArrayList<String> weeks, ArrayList<Integer> counts, Integer countWordsOfMonth, String message);
}
