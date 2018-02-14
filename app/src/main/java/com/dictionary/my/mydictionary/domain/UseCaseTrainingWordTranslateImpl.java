package com.dictionary.my.mydictionary.domain;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.repository.TrainingWordTranslateRepository;
import com.dictionary.my.mydictionary.data.repository.TrainingWordTranslateRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by luxso on 01.02.2018.
 */

public class UseCaseTrainingWordTranslateImpl implements UseCaseTrainingWordTranslate {
    TrainingWordTranslateRepository repository;
    private String wordMod;
    private String translateMod;
    private final int minCountOfWords = 10;
    private final int countOfReadyBlock = 5;
    private final int countOfReadyTranslate = 4;
    private int maxCountOfTryingFindWord = minCountOfWords;
    private int countOfTryingFindWord;
    private int maxCountOfTryingFindTranslate = minCountOfWords;
    private int countOfTryingFindTranslate;

    public UseCaseTrainingWordTranslateImpl(Context context, Map<String,String> languageMode, String KEY_WORD, String KEY_TRANSLATE){
        repository = new TrainingWordTranslateRepositoryImpl(context);
        wordMod = languageMode.get(KEY_WORD);
        translateMod = languageMode.get(KEY_TRANSLATE);
    }

    @Override
    public Observable<ArrayList<Map<String, Object>>> getTraining() {
        Observable<ArrayList<Map<String,Object>>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<Map<String, Object>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<Map<String, Object>>> e) throws Exception {
                try {
                    ArrayList<String> allIds = repository.getAllId();
                    int count = allIds.size();

                    ArrayList<Map<String, Object>> listOfWord = new ArrayList<Map<String, Object>>();
                    ArrayList<Long> idListOfWord = new ArrayList<>();
                    ArrayList<String> strListOfWord = new ArrayList<String>();
                    Map<String, Object> itemWord;
                    String word;

                    ArrayList<Map<String, Object>> listOfTranslate;
                    ArrayList<Long> idListOfTranslate;
                    ArrayList<String> strListOfTranslate;
                    Map<String, Object> itemTranslate;
                    String translate;
                    Map<String,Object> swapItemTranslate;

                    ArrayList<Map<String, Object>> emittedItems;

                    if (count >= minCountOfWords) {
                        Random rand = new Random();
                        long id;
                        // формируем блоки (слово - 5 переводов)
                        for(int i = 0; i < countOfReadyBlock; i++) {
                            listOfTranslate = new ArrayList<Map<String, Object>>();
                            idListOfTranslate = new ArrayList<>();
                            strListOfTranslate = new ArrayList<String>();
                            emittedItems = new ArrayList<Map<String, Object>>();
                            itemWord = new HashMap<>();
                            // рандомно выбираем id слова и проверяем его на совпадение с уже имеющимися
                            //(если количество попыток слишком большое, вызываем Exception)
                            countOfTryingFindWord = 0;
                            do {
                                if(countOfTryingFindWord > maxCountOfTryingFindWord){
                                    throw new Throwable();

                                }
                                id = Long.parseLong(allIds.get(rand.nextInt(count)));
                                if(wordMod.equals(Content.COLUMN_WORD)){
                                    word = repository.getWordById(id);
                                    itemWord.put(Content.COLUMN_WORD, word);
                                }else{
                                    word = repository.getTranslateById(id);
                                    itemWord.put(Content.COLUMN_TRANSLATE, word);
                                }
                                countOfTryingFindWord++;
                            } while (idListOfWord.contains(id) || strListOfWord.contains(word));
                            idListOfWord.add(id);
                            strListOfWord.add(word);
                            itemWord.put(Content.COLUMN_ROWID, id);
                            listOfWord.add(itemWord);
                            // выгружаем правильный перевод и записываем его на первую позицию в списке
                            itemTranslate = new HashMap<>();
                            if(translateMod.equals(Content.COLUMN_TRANSLATE)){
                                translate = repository.getTranslateById(id);
                                itemTranslate.put(Content.COLUMN_TRANSLATE, translate);
                            }else{
                                translate = repository.getWordById(id);
                                itemTranslate.put(Content.COLUMN_WORD, translate);
                            }
                            idListOfTranslate.add(id);
                            strListOfTranslate.add(translate);
                            itemTranslate.put(Content.COLUMN_ROWID, id);
                            listOfTranslate.add(itemTranslate);
                            // догружаем остальные 4 перевода
                            for(int j = 0; j < countOfReadyTranslate; j++) {
                                itemTranslate = new HashMap<>();
                                // рандомно выбираем id перевода и проверяем его на совпадение с уже имеющимися
                                //(если количество попыток слишком большое, вызываем Exception)
                                countOfTryingFindTranslate = 0;
                                do {
                                    if(countOfTryingFindTranslate > maxCountOfTryingFindTranslate){
                                        throw new Throwable();
                                    }
                                    id = Long.parseLong(allIds.get(rand.nextInt(count)));
                                    if(translateMod.equals(Content.COLUMN_TRANSLATE)){
                                        translate = repository.getTranslateById(id);
                                        itemTranslate.put(Content.COLUMN_TRANSLATE, translate);
                                    }else{
                                        translate = repository.getWordById(id);
                                        itemTranslate.put(Content.COLUMN_WORD, translate);
                                    }
                                    countOfTryingFindTranslate++;
                                } while (idListOfTranslate.contains(id) || strListOfTranslate.contains(translate));
                                idListOfTranslate.add(id);
                                strListOfTranslate.add(translate);
                                itemTranslate.put(Content.COLUMN_ROWID, id);
                                listOfTranslate.add(itemTranslate);
                            }
                            // перемещаем правильный перевод в случайную позицию в списке
                            int positionOfRightChoice = rand.nextInt(5);
                            swapItemTranslate = listOfTranslate.get(positionOfRightChoice);
                            listOfTranslate.set(positionOfRightChoice,listOfTranslate.get(0));
                            listOfTranslate.set(0,swapItemTranslate);
                            // формируем конечный блок
                            emittedItems.add(listOfWord.get(i));
                            emittedItems.addAll(listOfTranslate);
                            // выводим в лог сформированный блок
                            String str = new String();
                            for(Map<String,Object> logStr: emittedItems){
                                str += logStr.toString();
                                str += " ";
                            }
                            Log.d("LOG_TAG", "emittedItem is " + str);
                            if(!e.isDisposed()) {
                                e.onNext(emittedItems);
                            }
                        }
                    }
                    if(!e.isDisposed()) {
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

    @Override
    public void destroy() {
        repository.destroy();
    }
}
