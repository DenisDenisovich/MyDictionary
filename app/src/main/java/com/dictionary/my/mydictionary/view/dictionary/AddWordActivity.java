package com.dictionary.my.mydictionary.view.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 19.03.2018.
 */

public interface AddWordActivity {
    void showProgress();
    void hideProgress();
    void showERROR(String message);
    void setGroups(ArrayList<Group> groups);
    void createListOfTranslation(ArrayList<Translation> words);
    void setAlternativeTranslationMode();
    void setDefaultTranslationMode();
    String getPrintedWord();
    Translation getNewTranslation();

}
