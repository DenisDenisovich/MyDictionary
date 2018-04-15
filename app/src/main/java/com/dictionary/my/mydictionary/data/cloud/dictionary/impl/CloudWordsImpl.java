package com.dictionary.my.mydictionary.data.cloud.dictionary.impl;

import android.util.Log;

import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudWords;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningSkyEng;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningsWithSimilarTranslation;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.Meaning;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.WordSkyEng;
import com.dictionary.my.mydictionary.data.exception.MeaningIsNotFoundException;
import com.dictionary.my.mydictionary.data.exception.TranslationIsNotFoundException;
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

public class CloudWordsImpl implements CloudWords {
    private final static String LOG_TAG = "Log_CloudAllWords";
    @Override
    public ArrayList<Translation> getTranslation(String word) throws IOException, TranslationIsNotFoundException{
        Log.d(LOG_TAG, "getTranslation()");
        ArrayList<Translation> translations = new ArrayList<>();
        Response<ArrayList<WordSkyEng>> response = App.getSkyEngApi().getWord(word).execute();
        ArrayList<WordSkyEng> allPossibleWords = response.body();
        // if we get empty response
        if(allPossibleWords.size() == 0){
            Log.d(LOG_TAG, "response have empty body");
            throw new TranslationIsNotFoundException(word);
        }
        WordSkyEng wordSkyEng = null;
        for(WordSkyEng item: allPossibleWords){
            if(item.getText().equals(word)) {
                wordSkyEng = item;
                break;
            }
        }
        if(wordSkyEng == null){
            Log.d(LOG_TAG, "response doesn't have necessary word");
            throw new TranslationIsNotFoundException(word);
        }
        List<Meaning> meanings = wordSkyEng.getMeanings();
        for (int i = 0; i < meanings.size(); i++) {
            if (i > 4) {
                break;
            }
            Meaning meaning = meanings.get(i);
            Translation item = new Translation();
            item.setRus(meaning.getTranslation().getText());

            if(meaning.getSoundUrl() != null) {
                String urlSound = "http:";
                urlSound = urlSound.concat(meaning.getSoundUrl());
                item.setSound(urlSound);
            }else {
                item.setSound("");
            }

            if(meaning.getPreviewUrl() != null) {
                String urlPreviewImage = "http:";
                urlPreviewImage = urlPreviewImage.concat(meaning.getPreviewUrl());
                item.setPreview_image(urlPreviewImage);
            }else {
                item.setPreview_image("");
            }

            item.setMeaningId(String.valueOf(meaning.getId()));
            translations.add(item);
        }
        return translations;
    }

    @Override
    public WordFullInformation getMeaning(Translation translation) throws IOException, MeaningIsNotFoundException {
        Log.d(LOG_TAG, "getMeaning()");

        Log.d(LOG_TAG,translation.getEng());
        Log.d(LOG_TAG,translation.getRus());
        Log.d(LOG_TAG,translation.getSound());
        Log.d(LOG_TAG,translation.getPreview_image());
        Log.d(LOG_TAG,translation.getMeaningId());
        Log.d(LOG_TAG,translation.getDate());
        Log.d(LOG_TAG,String.valueOf(translation.getGroupId()));

        WordFullInformation word = new WordFullInformation();
        Response<ArrayList<MeaningSkyEng>> response = App.getSkyEngApi().getMeaning(translation.getMeaningId()).execute();
        ArrayList<MeaningSkyEng> meaningList = response.body();
        // if response have empty body
        if(meaningList.size() == 0){
            Log.d(LOG_TAG, "response have empty body");
            throw new MeaningIsNotFoundException(translation.getEng(), translation.getMeaningId());
        }
        MeaningSkyEng meaning = meaningList.get(0);

        word.setEng(translation.getEng());
        word.setRus(translation.getRus());
        word.setPreviewImage(translation.getPreview_image());
        word.setDate(translation.getDate());
        word.setGroupId(translation.getGroupId());
        word.setNote((String) meaning.getTranslation().getNote());
        word.setSound(translation.getSound());
        word.setTranscription(meaning.getTranscription());
        word.setPartOfSpeech(meaning.getPartOfSpeechCode());

        try{
            String urlImage = "http:";
            urlImage = urlImage.concat(meaning.getImages().get(0).getUrl());
            word.setImage(urlImage);
        }catch(NullPointerException e) {
            word.setImage("");
        }

        word.setDefinition(meaning.getDefinition().getText());

        try {
            ArrayList<String> alternativeTranslations = new ArrayList<>();
            List<MeaningsWithSimilarTranslation> mwst = meaning.getMeaningsWithSimilarTranslation();
            for (int i = 0; i < mwst.size(); i++) {
                if (i > 4) {
                    break;
                }
                alternativeTranslations.add(mwst.get(i).getTranslation().getText());
            }
            word.setAlternative(alternativeTranslations);
        }catch (NullPointerException e){
            word.setAlternative(null);
        }

        word.setExamples(meaning.getExamples());
        return word;
    }
}
