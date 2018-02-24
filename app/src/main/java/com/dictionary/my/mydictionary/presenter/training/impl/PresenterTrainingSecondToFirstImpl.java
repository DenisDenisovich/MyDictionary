package com.dictionary.my.mydictionary.presenter.training.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.entites.WordSecondToFirst;
import com.dictionary.my.mydictionary.domain.training.UseCaseTrainingSecondToFirst;
import com.dictionary.my.mydictionary.domain.training.impl.UseCaseTrainingSecondToFirstImpl;
import com.dictionary.my.mydictionary.presenter.training.PresenterTrainingSecondToFirst;
import com.dictionary.my.mydictionary.view.training.ViewTrainingSecondToFirst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 01.02.2018.
 */

public class PresenterTrainingSecondToFirstImpl<V extends ViewTrainingSecondToFirst> implements PresenterTrainingSecondToFirst<V> {

    private V view;
    private UseCaseTrainingSecondToFirst useCase;
    private ArrayList<WordSecondToFirst> items = new ArrayList<>();
    private final String KEY_ROWID = Content.COLUMN_ROWID;
    private final String KEY_TRANSLATE = Content.COLUMN_TRANSLATE;
    private int currentBlock = 0;
    private boolean pauseFlag = false;
    private int countOfRightAnswer = 0;
    private DisposableObserver<WordSecondToFirst> blockDisposable;
    public PresenterTrainingSecondToFirstImpl(Context context){
        useCase = new UseCaseTrainingSecondToFirstImpl(context);
    }
    @Override
    public void attach(V v) {
        Log.d("LOG_TAG_C/D_Training", "TrainingSecondToFirstPresenterImpl: attach(): Presenter: " + this.hashCode() + " View:" + v.hashCode());
        this.view = v;
    }

    @Override
    public void detach() {
        Log.d("LOG_TAG_C/D_Training", "TrainingSecondToFirstPresenterImpl: detach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        view = null;
    }

    @Override
    public void destroy() {
        Log.d("LOG_TAG", "TrainingSecondToFirstPresenterImpl: destroy()");
        if(blockDisposable != null){
            blockDisposable.dispose();
        }
        useCase.destroy();
    }

    @Override
    public void init() {
        Log.d("LOG_TAG", "TrainingSecondToFirstPresenterImpl: init()");
        view.showProgress();
        blockDisposable = useCase.getTraining()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WordSecondToFirst>() {
                    @Override
                    public void onNext(@NonNull WordSecondToFirst word) {
                        items.add(word);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.setErrorMessage("ERROR");

                    }

                    @Override
                    public void onComplete() {
                        if(items.size() != 0) {
                            view.hideProgress();
                            view.setWord(items.get(currentBlock).getWord());
                            ArrayList<Map<String, Object>> translate = new ArrayList<Map<String, Object>>();
                            Map<String, Object> item;
                            for (int i = 0; i < items.get(currentBlock).getTranslations().size(); i++) {
                                item = new HashMap<>();
                                item.put(KEY_TRANSLATE, items.get(currentBlock).getTranslations().get(i));
                                item.put(KEY_ROWID, items.get(currentBlock).getTranslationsId().get(i));
                                translate.add(item);
                            }
                            view.setTranslates(translate);
                        }else{
                            view.setErrorMessage("You do not have enough words for training");
                        }
                    }
                });
    }

    @Override
    public void update() {
        Log.d("LOG_TAG", "TrainingSecondToFirstPresenterImpl: update()");
        setPause()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        if(currentBlock < items.size()) {
                            view.setWord(items.get(currentBlock).getWord());
                            ArrayList<Map<String, Object>> translate = new ArrayList<Map<String, Object>>();
                            Map<String, Object> item;
                            for (int i = 0; i < items.get(currentBlock).getTranslations().size(); i++) {
                                item = new HashMap<>();
                                item.put(KEY_TRANSLATE, items.get(currentBlock).getTranslations().get(i));
                                item.put(KEY_ROWID, items.get(currentBlock).getTranslationsId().get(i));
                                translate.add(item);
                            }
                            view.setTranslates(translate);
                        } else if(currentBlock == items.size()){
                            String result;
                            result = String.valueOf(countOfRightAnswer) + "/" + String.valueOf(items.size());
                            view.setResultMessage(result);
                        }
                        pauseFlag = false;

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void TranslateIsSelected() {
        Log.d("LOG_TAG", "TrainingSecondToFirstPresenterImpl: TranslateIsSelected()");
        if(!pauseFlag) {
            long wordId = items.get(currentBlock).getRightTranslationId();
            long translateId = view.getSelectedTranslate();
            currentBlock++;
            if (wordId == translateId) {
                view.setPositiveAnswer();
                countOfRightAnswer++;
            } else {
                view.setNegativeAnswer();
            }
            update();
        }
    }

    private Completable setPause(){
        Log.d("LOG_TAG", "TrainingWordTranslatePresenterImpl setPause()");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    pauseFlag = true;
                    Thread.sleep(500);
                    e.onComplete();
                }catch (InterruptedException exc){
                    e.onError(exc);
                }
            }
        });
    }

}
