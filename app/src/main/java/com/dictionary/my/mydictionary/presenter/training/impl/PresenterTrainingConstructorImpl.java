package com.dictionary.my.mydictionary.presenter.training.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.domain.training.UseCaseTrainingConstructor;
import com.dictionary.my.mydictionary.domain.training.impl.UseCaseTrainingConstructorImpl;
import com.dictionary.my.mydictionary.data.entites.WordConstructor;
import com.dictionary.my.mydictionary.presenter.training.PresenterTrainingConstructor;
import com.dictionary.my.mydictionary.view.training.ViewTrainingConstructor;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 18.02.2018.
 */

public class PresenterTrainingConstructorImpl<V extends ViewTrainingConstructor> implements PresenterTrainingConstructor<V> {
    V view;
    private DisposableObserver<WordConstructor> blockDisposable;
    UseCaseTrainingConstructor useCase;
    private int currentBlock = 0;
    private String currentTranslate;
    private boolean pauseFlag = false;
    ArrayList<WordConstructor> items;
    public PresenterTrainingConstructorImpl(Context context){
        useCase = new UseCaseTrainingConstructorImpl(context);
    }
    @Override
    public void attach(V v) {
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorPresenterImpl: attach(): Presenter: " + this.hashCode() + " View:" + v.hashCode());
        this.view = v;
    }

    @Override
    public void detach() {
        Log.d("LOG_TAG_C/D_Training", "TrainingConstructorPresenterImpl: detach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        view = null;
    }

    @Override
    public void destroy() {
        if(blockDisposable != null){
            blockDisposable.dispose();
        }
        useCase.destroy();
        Log.d("LOG_TAG", "TrainingConstructorPresenterImpl destroy()");
    }

    @Override
    public void init() {
        Log.d("LOG_TAG", "ConstPres init()");
        currentTranslate = new String();
        items = new ArrayList<>();
        view.showProgress();
        blockDisposable = useCase.getTraining()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WordConstructor>() {
                    @Override
                    public void onNext(@NonNull WordConstructor word) {
                        items.add(word);
                        Log.d("LOG_TAG", "item id: " + word.getId() + ", word: " + word.getWord() +
                                ", translate: " + word.getTranslate() + ", symb: " + word.getCharsOfTranlate().toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.setErrorMessage("ERROR");
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                        if(items.size() == 0){
                            view.setErrorMessage("ERROR");
                        }else {
                            update();
                        }
                    }
                });
    }

    @Override
    public void update() {
        if(currentBlock == items.size()){
            view.setResultMessage("Training is done");
        }else{
            view.setWord(items.get(currentBlock).getWord());
            view.setCurrentTranslate(currentTranslate);
            view.setSymbols(items.get(currentBlock).getCharsOfTranlate());
        }
    }

    @Override
    public void symbolClick() {
        Log.d("LOG_TAG", "ConstPres symbolClick()");
        currentTranslate += view.getClickSymbol();
        view.setCurrentTranslate(currentTranslate);
        if(items.get(currentBlock).getTranslate().length() == currentTranslate.length()){
            TranslateIsBuild();
        }
    }

    @Override
    public void TranslateIsBuild() {
        Log.d("LOG_TAG", "ConstPres TranslateIsBuild()");
        if(items.get(currentBlock).getTranslate().toUpperCase().equals(currentTranslate)){
            view.setPositiveMessage();
            currentBlock++;
        }else{
            view.setNegativeMessage();
        }
        currentTranslate = "";
        setPause()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        update();
                    }
                });
    }

    private Completable setPause(){
        Log.d("LOG_TAG", "TrainingConstructorPresenterImpl setPause()");
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
