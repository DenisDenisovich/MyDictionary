package com.dictionary.my.mydictionary.data.cloud.dictionary;

import com.dictionary.my.mydictionary.data.exception.MeaningIsNotFoundException;
import com.dictionary.my.mydictionary.data.exception.TranslationIsNotFoundException;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by luxso on 17.03.2018.
 */

public interface CloudWords {
    Double MIN_FREQUENCY_PERCENT = 10.0;
    ArrayList<Translation> getTranslation(String word) throws IOException, TranslationIsNotFoundException;
    WordFullInformation getMeaning(Translation translation) throws IOException, MeaningIsNotFoundException;
}
