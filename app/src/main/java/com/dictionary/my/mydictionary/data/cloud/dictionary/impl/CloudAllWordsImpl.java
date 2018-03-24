package com.dictionary.my.mydictionary.data.cloud.dictionary.impl;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudAllWords;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningSkyEng;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningsWithSimilarTranslation;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.Meaning;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.WordSkyEng;
import com.dictionary.my.mydictionary.data.exception.SkyEngWordException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.view.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by luxso on 17.03.2018.
 */

public class CloudAllWordsImpl implements CloudAllWords{
    private final static String LOG_TAG = "Log_CloudAllWords";
    @Override
    public ArrayList<Translation> getTranslation(String word) throws Exception{
        Log.d(LOG_TAG, "getTranslation()");
        ArrayList<Translation> translations = new ArrayList<>();
        try {
            Response<ArrayList<WordSkyEng>> response = App.getSkyEngApi().getWord(word).execute();
            WordSkyEng wordSkyEng = response.body().get(0);
            List<Meaning> meanings = wordSkyEng.getMeanings();
            for (int i = 0; i < meanings.size(); i++) {
                if (i > 4) {
                    break;
                }
                Meaning meaning = meanings.get(i);
                Translation item = new Translation();
                item.setRus(meaning.getTranslation().getText());
                item.setSound(meaning.getSoundUrl());
                String urlImage = "http:";
                urlImage = urlImage.concat(meaning.getPreviewUrl());
                item.setPreview_image(urlImage);
                item.setMeaningId(String.valueOf(meaning.getId()));
                translations.add(item);
            }
        }catch (Exception exc){
            throw new SkyEngWordException(exc.toString());
        }

        return translations;
    }

    @Override
    public WordFullInformation getMeaning(Translation translation) throws Exception {
        Log.d(LOG_TAG, "getMeaning()");

        Log.d(LOG_TAG,translation.getEng());
        Log.d(LOG_TAG,translation.getRus());
        Log.d(LOG_TAG,translation.getSound());
        Log.d(LOG_TAG,translation.getPreview_image());
        Log.d(LOG_TAG,translation.getMeaningId());
        Log.d(LOG_TAG,translation.getDate());
        Log.d(LOG_TAG,String.valueOf(translation.getGroupId()));

        WordFullInformation word = new WordFullInformation();
        try {
            Response<ArrayList<MeaningSkyEng>> response = App.getSkyEngApi().getMeaning(translation.getMeaningId()).execute();
            MeaningSkyEng meaning = response.body().get(0);
            word.setEng(translation.getEng());
            word.setRus(translation.getRus());
            String urlImage = "http:";
            urlImage = urlImage.concat(translation.getPreview_image());
            word.setPreviewImage(urlImage);
            word.setSound(translation.getSound());
            word.setDate(translation.getDate());
            word.setGroupId(translation.getGroupId());
            word.setNote((String) meaning.getTranslation().getNote());
            word.setSound(meaning.getSoundUrl());
            word.setTranscription(meaning.getTranscription());
            word.setPartOfSpeech(meaning.getPartOfSpeechCode());
            word.setImage(meaning.getImages().get(0).getUrl());
            word.setDefinition(meaning.getDefinition().getText());
            ArrayList<String> alternativeTranslations = new ArrayList<>();
            List<MeaningsWithSimilarTranslation> mwst = meaning.getMeaningsWithSimilarTranslation();
            for (int i = 0; i < mwst.size(); i++) {
                if (i > 4) {
                    break;
                }
                alternativeTranslations.add(mwst.get(i).getTranslation().getText());
            }
            word.setAlternative(alternativeTranslations);
            word.setExamples(meaning.getExamples());
        }catch (Exception exc){
            throw new SkyEngWordException(exc.toString());
        }

        return word;
    }
}
