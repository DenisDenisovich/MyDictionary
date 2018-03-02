package com.dictionary.my.mydictionary.presenter.training.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.entites.WordSecondToFirst;
import com.dictionary.my.mydictionary.domain.training.UseCaseTrainingFirstToSecond;
import com.dictionary.my.mydictionary.domain.training.impl.UseCaseTrainingFirstToSecondImpl;
import com.dictionary.my.mydictionary.presenter.training.PresenterTrainingFirstToSecond;
import com.dictionary.my.mydictionary.view.training.ViewTrainingFirstToSecond;

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
 * Created by luxso on 23.02.2018.
 */

public class PresenterTrainingFirstToSecondImpl<V extends ViewTrainingFirstToSecond> implements PresenterTrainingFirstToSecond<V> {
    private V view;
    private UseCaseTrainingFirstToSecond useCase;
    private ArrayList<WordSecondToFirst> items = new ArrayList<>();
    private final String KEY_ROWID = Content.COLUMN_ROWID;
    private final String KEY_TRANSLATE = Content.COLUMN_TRANSLATE;
    private int currentBlock = 0;
    private boolean pauseFlag = false;
    private int countOfRightAnswer = 0;
    private DisposableObserver<WordSecondToFirst> blockDisposable;
    public PresenterTrainingFirstToSecondImpl(Context context){
        useCase = new UseCaseTrainingFirstToSecondImpl(context);
    }
    @Override
    public void attach(V v) {
        Log.d("LOG_TAG_C/D_Training", "TrainingFirstToSecondPresenterImpl: attach(): Presenter: " + this.hashCode() + " View:" + v.hashCode());
        this.view = v;
    }

    @Override
    public void detach() {
        Log.d("LOG_TAG_C/D_Training", "TrainingFirstToSecondPresenterImpl: detach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        view = null;
    }

    @Override
    public void destroy() {
        Log.d("LOG_TAG", "TrainingFirstToSecondPresenterImpl: destroy()");
        if(blockDisposable != null){
            blockDisposable.dispose();
        }
        useCase.destroy();
    }

    @Override
    public void init() {
        Log.d("LOG_TAG", "TrainingFirstToSecondPresenterImpl: init()");
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
        Log.d("LOG_TAG", "TrainingFirstToSecondPresenterImpl: update()");
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
    public void translateIsSelected() {
        Log.d("LOG_TAG", "TrainingFirstToSecondPresenterImpl: translateIsSelected()");
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
        Log.d("LOG_TAG", "TrainingFirstToSecondPresenterImpl: setPause()");
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
