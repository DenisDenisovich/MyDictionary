package com.dictionary.my.mydictionary.domain.training.impl;

import android.content.Context;

import com.dictionary.my.mydictionary.data.repository.storage.TrainingRepository;
import com.dictionary.my.mydictionary.data.repository.storage.TrainingRepositoryImpl;
import com.dictionary.my.mydictionary.data.entites.skyengapi.WordSkyEng;
import com.dictionary.my.mydictionary.data.entites.WordSprint;
import com.dictionary.my.mydictionary.domain.training.UseCaseTrainingSprint;
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
 * Created by luxso on 19.02.2018.
 */

public class UseCaseTrainingSprintImpl implements UseCaseTrainingSprint {
    private TrainingRepository repository;
    Response<ArrayList<WordSkyEng>> response;
    ArrayList<String> alternativeTranslate;
    private final int minCountOfWords = 10;
    private final int countOfBlock = 40;
    private int maxCountOfTryingFindWord = minCountOfWords;
    private int countOfTryingFindWord;
    public UseCaseTrainingSprintImpl(Context context){
        repository = new TrainingRepositoryImpl(context);
    }
    @Override
    public Observable<WordSprint> getTraining() {
        return Observable.create(new ObservableOnSubscribe<WordSprint>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<WordSprint> e) throws Exception {
                try{
                    ArrayList<String> allIds = repository.getAllId();
                    int count = allIds.size();
                    WordSprint item;
                    String word;
                    String translate;
                    long id;
                    if(count >= minCountOfWords){
                        Random rand = new Random();
                        for(int i = 0; i < countOfBlock; i++){
                            item = new WordSprint();
                            id = Long.valueOf(allIds.get(rand.nextInt(allIds.size())));
                            word = repository.getTranslateById(id);
                            item.setWord(word);
                            item.setId(id);
                            // выгружаем из SkyEng альтернативные переводы
                            alternativeTranslate = getAlternativeTranslates(word.toLowerCase());
                            if (rand.nextInt()%2 == 0){
                                // вставляем правильный перевод
                                item.setTranslate(repository.getWordById(id));
                                item.setRightFlag(true);
                            }else{
                                // вставляем неправильный перевод
                                countOfTryingFindWord = 0;
                                do {
                                    if(countOfTryingFindWord > maxCountOfTryingFindWord){
                                        throw new Throwable();
                                    }
                                    id = Long.valueOf(allIds.get(rand.nextInt(allIds.size())));
                                    translate = repository.getWordById(id);
                                    countOfTryingFindWord++;
                                }while (translate.toLowerCase().equals(repository.getWordById(item.getId()).toLowerCase())
                                        || alternativeTranslate.contains(translate.toLowerCase()));
                                    item.setTranslate(repository.getWordById(id));
                                    item.setRightFlag(false);
                            }
                            if(!e.isDisposed()){
                                e.onNext(item);
                            }
                        }
                        if(!e.isDisposed()){
                            e.onComplete();
                        }
                    }else{
                        if(!e.isDisposed()){
                            e.onComplete();
                        }
                    }
                }catch (Throwable t){
                    if(!e.isDisposed()){
                        e.onError(t);
                    }
                }
            }
        });
    }

    private ArrayList<String> getAlternativeTranslates(String word) throws IOException {
        alternativeTranslate = new ArrayList<String>();
        ArrayList<WordSkyEng> data;
        response = App.getSkyEngApi().getAlterTranslations(word).execute();
        data = response.body();
        for(WordSkyEng w: data){
            alternativeTranslate.add(w.getText().toLowerCase());
        }
        return alternativeTranslate;
    }

    @Override
    public void destroy() {
        repository.destroy();
    }
}
