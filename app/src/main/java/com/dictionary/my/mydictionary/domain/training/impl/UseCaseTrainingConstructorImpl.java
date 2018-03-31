package com.dictionary.my.mydictionary.domain.training.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.db.training.DBTraining;
import com.dictionary.my.mydictionary.data.db.training.impl.DBTrainingImpl;
import com.dictionary.my.mydictionary.domain.entites.training.WordConstructor;
import com.dictionary.my.mydictionary.domain.training.UseCaseTrainingConstructor;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by luxso on 18.02.2018.
 */

public class UseCaseTrainingConstructorImpl implements UseCaseTrainingConstructor {
    DBTraining repository;
    private final int minCountOfWords = 10;
    private final int countOfBlock = 5;
    private int maxCountOfTryingFindWord = minCountOfWords;
    private int countOfTryingFindWord;
    public UseCaseTrainingConstructorImpl(Context context){
        repository = new DBTrainingImpl(context);
    }
    @Override
    public Observable<WordConstructor> getTraining() {
        return Observable.create(new ObservableOnSubscribe<WordConstructor>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<WordConstructor> e) throws Exception {
                try{
                    ArrayList<String> allIds = repository.getAllId();
                    int count = allIds.size();

                    WordConstructor itemWord;
                    ArrayList<String> strListOfWords = new ArrayList<String>();
                    String word;

                    String translate;
                    ArrayList<String> chars; // список, куда мы копируем буквы, из которых состоит перевод
                    int indexOfChar;
                    ArrayList<String> charsOfTranslate; // конечный список рандомно перемешанных букв
                    String symbol;
                    if(count >= minCountOfWords) {
                        Random rand = new Random();
                        long id;
                        for (int i = 0; i < countOfBlock; i++) {
                            itemWord = new WordConstructor();
                            countOfTryingFindWord = 0;
                            // выбираем новое слово для блока
                            do {
                                // если количество попыток поиска превышает 10, то выкидываем ошибку
                                if(countOfTryingFindWord > maxCountOfTryingFindWord){
                                    throw new Throwable();
                                }
                                id = Long.parseLong(allIds.get(rand.nextInt(count)));
                                word = repository.getTranslateById(id);
                                countOfTryingFindWord++;
                            }while (strListOfWords.contains(word.toLowerCase()));
                            strListOfWords.add(word.toLowerCase());
                            translate = repository.getWordById(id);
                            itemWord.setId(id);
                            itemWord.setWord(word);
                            itemWord.setTranslate(translate);
                            chars = new ArrayList<String>();
                            // формируем список букв перевода
                            for(int j = 0; j < translate.length(); j++){
                                chars.add(String.valueOf(translate.charAt(j)).toUpperCase());
                            }
                            charsOfTranslate = new ArrayList<String>();
                            // рандомно выбираем из chars букву, удаляем ее и записываем в конечный список
                            for(int j = 0; j < translate.length()-1; j++){
                                indexOfChar = rand.nextInt(chars.size() - 1);
                                symbol = String.valueOf(chars.get(indexOfChar));
                                chars.remove(indexOfChar);
                                chars.trimToSize();
                                charsOfTranslate.add(symbol);
                            }
                            symbol = String.valueOf(chars.get(0));
                            chars.remove(0);
                            chars.trimToSize();
                            charsOfTranslate.add(symbol);
                            itemWord.setCharsOfTranlate(charsOfTranslate);
                            if(!e.isDisposed()){
                                e.onNext(itemWord);
                            }
                        }
                    }
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (Throwable t){
                    if(!e.isDisposed()){
                        e.onError(t);
                    }
                }
            }
        });
    }

    @Override
    public void destroy() {
        repository.destroy();
    }
}
