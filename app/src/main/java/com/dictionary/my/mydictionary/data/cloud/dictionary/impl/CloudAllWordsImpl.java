package com.dictionary.my.mydictionary.data.cloud.dictionary.impl;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.cloud.dictionary.CloudAllWords;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningSkyEng;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningsWithSimilarTranslation;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.Meaning;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.WordSkyEng;
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
    @Override
    public ArrayList<Translation> getTranslation(String word) throws IOException{
        ArrayList<Translation> translations = new ArrayList<>();
        Response<ArrayList<WordSkyEng>> response = App.getSkyEngApiWord().getWord(word).execute();
        WordSkyEng wordSkyEng = response.body().get(0);
        List<Meaning> meanings = wordSkyEng.getMeanings();

        for(int i = 0; i < meanings.size(); i++){
            if(i > 4){
                break;
            }
            Meaning meaning = meanings.get(i);
            Translation item = new Translation();
            item.setRus(meaning.getTranslation().getText());
            item.setSound(meaning.getSoundUrl());
            item.setPreview_image(meaning.getPreviewUrl());
            item.setMeaningId(meaning.getId());
            translations.add(item);
        }

        return translations;
    }

    @Override
    public WordFullInformation getMeaning(Translation translation) throws IOException {
        WordFullInformation word = new WordFullInformation();
        Response<MeaningSkyEng> response = App.getSkyEngApiWord().getMeaning(translation.getMeaningId()).execute();
        MeaningSkyEng meaning = response.body();

        word.setEng(translation.getEng());
        word.setRus(translation.getRus());
        word.setPreviewImage(translation.getPreview_image());
        word.setSound(translation.getSound());
        word.setDate(translation.getDate());
        word.setGroupId(translation.getGroupId());
        word.setNote((String)meaning.getTranslation().getNote());
        word.setSound(meaning.getSoundUrl());
        word.setTranscription(meaning.getTranscription());
        word.setPartOfSpeech(meaning.getPartOfSpeechCode());
        word.setImage(meaning.getImages().get(0).getUrl());
        word.setDefinition(meaning.getDefinition().getText());

        ArrayList<String> alternativeTranslations = new ArrayList<>();
        List<MeaningsWithSimilarTranslation> mwst = meaning.getMeaningsWithSimilarTranslation();
        for(int i = 0; i < 5; i++){
            if(i > 4){
                break;
            }
            alternativeTranslations.add(mwst.get(i).getTranslation().getText());
        }

        word.setAlternative(alternativeTranslations);
        word.setExamples(meaning.getExamples());

        return word;
    }
}
