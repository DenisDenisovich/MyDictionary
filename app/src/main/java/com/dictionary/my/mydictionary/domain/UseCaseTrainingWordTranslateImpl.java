package com.dictionary.my.mydictionary.domain;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.repository.TrainingWordTranslateRepository;
import com.dictionary.my.mydictionary.data.repository.TrainingWordTranslateRepositoryImpl;
import com.dictionary.my.mydictionary.domain.UseCaseTrainingWordTranslate;

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
    String givenWord;
    String choiceTranslatingWords;
    TrainingWordTranslateRepository repository;

    public UseCaseTrainingWordTranslateImpl(Context context, String[] languagesMode){
        repository = new TrainingWordTranslateRepositoryImpl(context);
        givenWord = languagesMode[0];
        choiceTranslatingWords = languagesMode[1];
    }
    @Override
    public Observable<ArrayList<Map<String, Object>>> getTraining() {
        Observable<ArrayList<Map<String,Object>>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<Map<String, Object>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<Map<String, Object>>> e) throws Exception {
                try {
                    ArrayList<String> allIds = repository.getAllId();
                    ArrayList<Map<String, Object>> listOfGivenWord = new ArrayList<Map<String, Object>>();
                    ArrayList<Long> idListOfGivenWord = new ArrayList<>();
                    int count = allIds.size();
                    long id;

                    if (count > 10) {
                        Random rand = new Random();
                        int countOfReadyBlock = 0;
                        while (countOfReadyBlock < 5) {
                            ArrayList<Map<String, Object>> listOfTranslWord = new ArrayList<Map<String, Object>>();
                            ArrayList<Long> idListOfTranslWord = new ArrayList<>();
                            ArrayList<Map<String, Object>> emittedItem = new ArrayList<Map<String, Object>>();
                            Map<String, Object> itemWord = new HashMap<>();
                            do {
                                id = Long.parseLong(allIds.get(rand.nextInt(count)));
                            } while (idListOfGivenWord.contains(id));
                            idListOfGivenWord.add(id);
                            if (givenWord == Content.COLUMN_WORD) {
                                String word = repository.getWordById(id);
                                itemWord.put(Content.COLUMN_ROWID, id);
                                itemWord.put(Content.COLUMN_WORD, word);
                                listOfGivenWord.add(itemWord);
                            } else {
                                String translate = repository.getTranslateById(id);
                                itemWord.put(Content.COLUMN_ROWID, id);
                                itemWord.put(Content.COLUMN_TRANSLATE, translate);
                                listOfGivenWord.add(itemWord);
                            }
                            //Log.d("LOG_TAG", "itemWord in USeCase" + itemWord.toString());
                            int countOfReadyTranslate = 0;
                            int positionOfRightChoice = rand.nextInt(5);
                            long idOfRightChoice = id;
                            while (countOfReadyTranslate < 5) {
                                Map<String, Object> itemTranslate = new HashMap<>();
                                if (countOfReadyTranslate == positionOfRightChoice && !idListOfTranslWord.contains(idOfRightChoice)) {
                                    id = idOfRightChoice;
                                } else {
                                    do {
                                        id = Long.parseLong(allIds.get(rand.nextInt(count)));
                                    } while (idListOfTranslWord.contains(id));
                                }
                                idListOfTranslWord.add(id);
                                if (choiceTranslatingWords == Content.COLUMN_WORD) {
                                    String word = repository.getWordById(id);
                                    itemTranslate.put(Content.COLUMN_ROWID, id);
                                    itemTranslate.put(Content.COLUMN_WORD, word);
                                    listOfTranslWord.add(itemTranslate);
                                } else {
                                    String translate = repository.getTranslateById(id);
                                    itemTranslate.put(Content.COLUMN_ROWID, id);
                                    itemTranslate.put(Content.COLUMN_TRANSLATE, translate);
                                    listOfTranslWord.add(itemTranslate);
                                }
                                //Log.d("LOG_TAG", "itemTranslate in USeCase" + itemTranslate.toString());
                                countOfReadyTranslate++;
                            }
                            emittedItem.add(listOfGivenWord.get(countOfReadyBlock));
                            emittedItem.addAll(listOfTranslWord);
                            String str = new String();
                            for(Map<String,Object> i: emittedItem){
                                str += i.toString();
                                str += " ";
                            }
                            Log.d("LOG_TAG", "emittedItem is " + str);
                            e.onNext(emittedItem);
                            countOfReadyBlock++;
                        }
                    }
                    e.onComplete();
                }catch (Throwable t){
                    e.onError(t);
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
