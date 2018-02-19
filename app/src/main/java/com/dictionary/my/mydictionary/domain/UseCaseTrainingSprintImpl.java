package com.dictionary.my.mydictionary.domain;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.repository.TrainingRepository;
import com.dictionary.my.mydictionary.data.repository.TrainingRepositoryImpl;
import com.dictionary.my.mydictionary.domain.entites.WordSprint;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by luxso on 19.02.2018.
 */

public class UseCaseTrainingSprintImpl implements UseCaseTrainingSprint {
    private TrainingRepository repository;
    private final int minCountOfWords = 10;
    private final int countOfBlock = 60;
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
                    long id;
                    if(count >= minCountOfWords){
                        Random rand = new Random();
                        for(int i = 0; i < countOfBlock; i++){
                            item = new WordSprint();
                            id = Long.valueOf(allIds.get(rand.nextInt(allIds.size())));
                            item.setWord(repository.getTranslateById(id));
                            if (rand.nextInt()%2 == 0){
                                // вставляем правильный перевод
                                item.setTranslate(repository.getWordById(id));
                                item.setRightFlag(true);
                            }else{
                                // вставляем неправильный перевод
                                id = Long.valueOf(allIds.get(rand.nextInt(allIds.size())));
                                item.setTranslate(repository.getWordById(id));
                                item.setRightFlag(false);
                            }
                            if(!e.isDisposed()){
                                Log.d("LOG_TAG", "item word: " + item.getWord() + ", translate: " + item.getTranslate() + ", boolean: " + item.isRightFlag());
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

    @Override
    public void destroy() {
        repository.destroy();
    }
}
