package com.dictionary.my.mydictionary.presenter.trainings;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.domain.UseCaseTrainingSprint;
import com.dictionary.my.mydictionary.domain.UseCaseTrainingSprintImpl;
import com.dictionary.my.mydictionary.domain.entites.WordSprint;
import com.dictionary.my.mydictionary.view.training.TrainingSprint;

import java.util.ArrayList;
import java.util.StringTokenizer;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 19.02.2018.
 */

public class TrainingSprintPresenterImpl<V extends TrainingSprint> implements TrainingSprintPresenter<V> {
    UseCaseTrainingSprint useCase;
    V view;
    DisposableObserver<WordSprint> blockDisposable;
    DisposableObserver<String> timeDisposable;
    ArrayList<WordSprint> items;
    int currentBlock = 0;
    int countOfRightSelected = 0;
    boolean pauseFlag = true;
    boolean taskIsDone = false;
    public TrainingSprintPresenterImpl(Context context){
        useCase = new UseCaseTrainingSprintImpl(context);
    }
    @Override
    public void attach(V v) {
        view = v;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void destroy() {
        if(blockDisposable != null) {
            blockDisposable.dispose();
        }
        if(timeDisposable != null){
            timeDisposable.dispose();
        }
        useCase.destroy();
    }

    @Override
    public void init() {
        view.showProgress();
        items = new ArrayList<>();
        blockDisposable = useCase.getTraining()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WordSprint>() {
                    @Override
                    public void onNext(@NonNull WordSprint wordSprint) {
                        items.add(wordSprint);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.setErrorMessage("ERROR");
                    }

                    @Override
                    public void onComplete() {
                        update();
                        setTime();

                    }
                });

    }

    @Override
    public void update() {
        if(!taskIsDone) {
            view.setWord(items.get(currentBlock).getWord());
            view.setTranslate(items.get(currentBlock).getTranslate());
        }
    }

    @Override
    public void rightButtonClick() {
        if(items.get(currentBlock).isRightFlag()){
            countOfRightSelected++;
            view.setPositiveMessage();
        }else{
            view.setNegativeMessage();
        }
        if(pauseFlag) {
            setPause()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            currentBlock++;
                            update();
                            pauseFlag = true;
                        }
                    });
        }
    }

    @Override
    public void wrongButtonClick() {
        if(items.get(currentBlock).isRightFlag()){
            view.setNegativeMessage();
        }else{
            countOfRightSelected++;
            view.setPositiveMessage();
        }
        if (pauseFlag) {
            setPause()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            currentBlock++;
                            update();
                            pauseFlag = true;
                        }
                    });
        }
    }

    private Completable setPause(){
        Log.d("LOG_TAG", "TrainingSprintPresenterImpl setPause()");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    pauseFlag = false;
                    Thread.sleep(500);
                    e.onComplete();
                }catch (InterruptedException exc){
                    e.onError(exc);
                }
            }
        });
    }
    private void setTime(){
        Log.d("LOG_TAG", "TrainingSprintPresenterImpl setTime()");
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                try {
                    int time = 30;
                    int i = 0;
                    while(i < time) {
                        if(pauseFlag && view != null) {
                            if (!e.isDisposed()) {
                                e.onNext(String.valueOf(time - i));
                            }
                            Thread.sleep(1000);
                            i++;
                        }
                    }
                    if(!e.isDisposed()){
                        e.onComplete();
                    }
                }catch (InterruptedException exc){
                    if(!e.isDisposed()) {
                        e.onError(exc);
                    }
                }
            }
        });
        timeDisposable = observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String time) {
                if(view != null){
                    Log.d("LOG_TAG", "currentTime: " + time);
                    view.setTime(time);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(view != null){
                    view.setErrorMessage("TimeERROR");
                }
            }

            @Override
            public void onComplete() {
                taskIsDone = true;
                view.setResultMessage(String.valueOf(countOfRightSelected));
            }
        });
    }
}
