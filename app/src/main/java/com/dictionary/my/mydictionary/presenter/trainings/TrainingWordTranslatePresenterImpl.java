package com.dictionary.my.mydictionary.presenter.trainings;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.UseCaseTrainingWordTranslate;
import com.dictionary.my.mydictionary.domain.UseCaseTrainingWordTranslateImpl;
import com.dictionary.my.mydictionary.view.training.TrainingWordTranslate;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 01.02.2018.
 */

public class TrainingWordTranslatePresenterImpl<V extends TrainingWordTranslate> implements TrainingWordTranslatePresenter<V>{

    private V view;
    private UseCaseTrainingWordTranslate useCase;
    private ArrayList<ArrayList<Map<String,Object>>> items = new ArrayList<ArrayList<Map<String,Object>>>();
    private int currentBlock = 0;
    private boolean pauseFlag = false;
    private int countOfRightAnswer = 0;
    private DisposableObserver<ArrayList<Map<String,Object>>> blockDisposable;
    public TrainingWordTranslatePresenterImpl(Context context, String[] languagesMode){
        useCase = new UseCaseTrainingWordTranslateImpl(context, languagesMode);
    }
    @Override
    public void attach(V v) {
        Log.d("LOG_TAG_C/D_Training", "TrainingWordTranslatePresenterImpl: attach(): Presenter: " + this.hashCode() + " View:" + v.hashCode());
        this.view = v;
    }

    @Override
    public void detach() {
        Log.d("LOG_TAG_C/D_Training", "TrainingWordTranslatePresenterImpl: detach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        view = null;
    }

    @Override
    public void destroy() {
        if(blockDisposable != null){
            blockDisposable.dispose();
        }
        useCase.destroy();
    }

    @Override
    public void init() {
        blockDisposable = useCase.getTraining()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArrayList<Map<String, Object>>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<Map<String, Object>> maps) {
                        view.showProgress();
                        String str = new String();
                        for(Map<String,Object> i: maps){
                            str += i.toString();
                            str += " ";
                        }
                        Log.d("LOG_TAG", "in onNext " + str);
                        items.add(maps);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                        view.setWord(items.get(currentBlock).get(0));
                        ArrayList<Map<String,Object>> translate = new ArrayList<Map<String, Object>>();
                        for(int i = 1; i < items.get(currentBlock).size();i++){
                            translate.add(items.get(currentBlock).get(i));
                        }
                        view.setTranslates(translate);
                    }
                });
    }

    @Override
    public void update() {
        setPause()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        if(currentBlock < items.size()) {
                            view.setWord(items.get(currentBlock).get(0));
                            ArrayList<Map<String, Object>> translate = new ArrayList<Map<String, Object>>();
                            for (int i = 1; i < items.get(currentBlock).size(); i++) {
                                translate.add(items.get(currentBlock).get(i));
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
        if(!pauseFlag) {
            long wordId = (Long) items.get(currentBlock).get(0).get(Content.COLUMN_ROWID);
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
