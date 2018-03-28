package com.dictionary.my.mydictionary.data.db.dictionary;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;

import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by luxso on 30.09.2017.
 */

public interface DBWords {
    ArrayList<Word> getListOfWord() throws Exception;
    void setNewWord(WordFullInformation word) throws Exception;
    void setNewWordWithoutInternet(Translation translation) throws Exception;
    void deleteWords(ArrayList<Long> delList) throws Exception;
    void moveWords(ArrayList<Long> moveList) throws Exception;
    void editWord(Word word) throws Exception;
    void destroy();
}
