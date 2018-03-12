package com.dictionary.my.mydictionary.domain.training.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.entites.training.WordSecondToFirst;
import com.dictionary.my.mydictionary.data.storage.training.DBTraining;
import com.dictionary.my.mydictionary.data.storage.training.impl.DBTrainingImpl;
import com.dictionary.my.mydictionary.data.entites.skyengapi.word.Meaning;
import com.dictionary.my.mydictionary.data.entites.skyengapi.word.WordSkyEng;
import com.dictionary.my.mydictionary.domain.training.UseCaseTrainingSecondToFirst;
import com.dictionary.my.mydictionary.view.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;

/**
 * Created by luxso on 01.02.2018.
 */

public class UseCaseTrainingSecondToFirstImpl implements UseCaseTrainingSecondToFirst {
    private DBTraining repository;
    private ArrayList<String> alternativeTranslate;
    private static final int MIN_COUNT_OF_WORDS = 10;
    private static final int COUNT_OF_READY_BLOCK = 5;
    private static final int COUNT_OF_REQUIRED_TRANSLATE = 4;
    private int maxCountOfTryingFindWord = MIN_COUNT_OF_WORDS;
    private int countOfTryingFindWord;
    private int maxCountOfTryingFindTranslate = MIN_COUNT_OF_WORDS;
    private int countOfTryingFindTranslate;
    public UseCaseTrainingSecondToFirstImpl(Context context){
        repository = new DBTrainingImpl(context);
    }

    @Override
    public Observable<WordSecondToFirst> getTraining() {
        Observable<WordSecondToFirst> observable = Observable.create(new ObservableOnSubscribe<WordSecondToFirst>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<WordSecondToFirst> e) throws Exception {
                try {
                    ArrayList<String> allIds = repository.getAllId();
                    int count = allIds.size();

                    ArrayList<Long> idListOfWord = new ArrayList<>();
                    ArrayList<String> strListOfWord = new ArrayList<String>();
                    ArrayList<String> strListOfTranslation = new ArrayList<String>();
                    String word;
                    String translation;
                    String checkString; //строка, через которую проверяем различность переводов двух слов

                    if (count >= MIN_COUNT_OF_WORDS) {
                        Random rand = new Random();
                        long id;
                        // формируем блоки (слово - 5 переводов)
                        for (int i = 0; i < COUNT_OF_READY_BLOCK; i++) {
                            WordSecondToFirst item = new WordSecondToFirst();
                            // рандомно выбираем id слова и проверяем его на совпадение с уже имеющимися
                            //(если количество попыток слишком большое, вызываем Exception)
                            countOfTryingFindWord = 0;
                            do {
                                if (countOfTryingFindWord > maxCountOfTryingFindWord) {
                                    throw new Throwable();
                                }
                                id = Long.parseLong(allIds.get(rand.nextInt(count)));
                                word = repository.getWordById(id);
                                countOfTryingFindWord++;
                            }
                            while (idListOfWord.contains(id) || strListOfWord.contains(word.toLowerCase()));
                            idListOfWord.add(id);
                            strListOfWord.add(word.toLowerCase());
                            item.setWord(id,word);
                            // выгружаем из SkyEng альтернативные переводы
                            alternativeTranslate = getAlternativeTranslates(word.toLowerCase());
                            // выгружаем правильный перевод
                            translation = repository.getTranslateById(id);
                            strListOfTranslation.add(translation.toLowerCase());
                            item.setRightTranslation(id,translation);
                            // догружаем остальные 4 перевода
                            for (int j = 0; j < COUNT_OF_REQUIRED_TRANSLATE; j++) {
                                // рандомно выбираем id перевода и проверяем его на совпадение с уже имеющимися
                                //(если количество попыток слишком большое, вызываем Exception)
                                countOfTryingFindTranslate = 0;
                                do {
                                    if (countOfTryingFindTranslate > maxCountOfTryingFindTranslate) {
                                        throw new Throwable();
                                    }
                                    id = Long.parseLong(allIds.get(rand.nextInt(count)));
                                    translation = repository.getTranslateById(id);
                                    checkString = repository.getWordById(id).toLowerCase();
                                    countOfTryingFindTranslate++;
                                }
                                while (item.getTranslationsId().contains(id) || strListOfTranslation.contains(translation.toLowerCase())
                                        || item.getWord().toLowerCase().equals(checkString)
                                        || alternativeTranslate.contains(translation.toLowerCase()));

                                strListOfTranslation.add(translation.toLowerCase());
                                item.setNewTranslate(id,translation);
                            }
                            item.changeRightTranslationIndex();
                            String str = new String();
                            str += "word: " + item.getWord() + ", id: " + item.getWordId() + " | ";
                            for (int k = 0; k < item.getTranslations().size(); k++) {
                                str += "translate: " + item.getTranslations().get(k)
                                    + ", id: " + item.getTranslationsId().get(k) + " |";
                            }
                            if (!e.isDisposed()) {
                                e.onNext(item);
                            }
                            strListOfTranslation.clear();
                        }
                    }
                    if (!e.isDisposed()) {
                        e.onComplete();
                    }
                }catch (Throwable t){
                    if(!e.isDisposed()) {
                        e.onError(t);
                    }
                }
            }
        });

        return observable;
    }

    private ArrayList<String> getAlternativeTranslates(String word) throws IOException{
        Response<ArrayList<WordSkyEng>> response;
        alternativeTranslate = new ArrayList<String>();
        ArrayList<WordSkyEng> data;
        response = App.getSkyEngApi().getWord(word).execute();
        data = response.body();
        for(Meaning m: data.get(0).getMeanings()){
            alternativeTranslate.add(m.getTranslation().getText().toLowerCase());
        }

        return alternativeTranslate;
    }

    @Override
    public void destroy() {
        repository.destroy();
    }
}
