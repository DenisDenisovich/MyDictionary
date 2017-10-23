package com.dictionary.my.mydictionary.presenter.dictionary;

import android.content.Context;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.UseCaseAllDictionaries;
import com.dictionary.my.mydictionary.domain.UseCaseAllDictionariesImpl;
import com.dictionary.my.mydictionary.view.dictionary.AllDictionaries;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;



public class AllDictionariesPresenterImpl<V extends AllDictionaries> implements AllDictionariesPresenter<V> {
    private UseCaseAllDictionaries useCase;
    private V view;
    private String[] from = Content.fromAllDictionaries;
    private ArrayList<Map<String,Object>> data;
    private DisposableObserver<Map<String,Object>> getDictionariesListDisposable;
    private Map<String, Object> newDict;
    private ArrayList<Long> idOfDelDicts;
    private Long idOfDelDictwithWords;
    private Map<String, Object> modifiedDictionary;

    public AllDictionariesPresenterImpl(Context context){
        useCase = new UseCaseAllDictionariesImpl(context);
    }

    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void destroy() {
        getDictionariesListDisposable.dispose();
        useCase.destroy();
    }

    @Override
    public void init() {
        data = new ArrayList<>();
        getDictionariesListDisposable = useCase.getDictionariesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Map<String, Object>>() {
            @Override
            public void onNext(@NonNull Map<String, Object> stringObjectMap) {
                data.add(stringObjectMap);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.showToast("Data I/O error");
            }

            @Override
            public void onComplete() {
                view.createAdapter(data,from);
                view.createDictionariesList();

            }
        });
    }



    @Override
    public void update() {
        view.setData(data);
        view.createDictionariesList();
    }


    @Override
    public void newDictionary() {
        newDict = view.getNewDictionary();
        Single<Map<String,Object>> observable = Single.create(new SingleOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Map<String, Object>> e) throws Exception {
                try {
                    e.onSuccess(newDict);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast("ERROR: adding new dictionary");
                }
            }
        });

        useCase.setNewDictionary(observable);
    }

    @Override
    public void deleteDictionary() {
        idOfDelDicts = view.getDeletingDictionary();
        Single<ArrayList<Long>> observable = Single.create(new SingleOnSubscribe<ArrayList<Long>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ArrayList<Long>> e) throws Exception {
                try {
                    e.onSuccess(idOfDelDicts);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast("ERROR: delete selecting dictionaries");
                }
            }
        });
        useCase.deleteDictionaries(observable);
    }

    @Override
    public void deleteDictionaryWithWords() {
        idOfDelDictwithWords = view.getDeletingDictionary().get(0);
        Single<Long> observable = Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Long> e) throws Exception {
                try{
                    e.onSuccess(idOfDelDictwithWords);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast("ERROR: delete selecting dictionary");
                }
            }
        });
        useCase.deleteDictionaryWithWords(observable);
    }

    @Override
    public void editDictionary() {
        modifiedDictionary = view.getEditingDictionary();
        Single<Map<String, Object>> observable = Single.create(new SingleOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Map<String, Object>> e) throws Exception {
                try{
                    e.onSuccess(modifiedDictionary);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast("ERROR: edit selecting dictionary");
                }
            }
        });
        useCase.editDictionary(observable);
    }
}
