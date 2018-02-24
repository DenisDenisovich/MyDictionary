package com.dictionary.my.mydictionary.presenter.dictionary.impl;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.dictionary.UseCaseAllDictionaries;
import com.dictionary.my.mydictionary.domain.dictionary.impl.UseCaseAllDictionariesImpl;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAllDictionaries;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllDictionaries;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;



public class PresenterAllDictionariesImpl<V extends ViewAllDictionaries> implements PresenterAllDictionaries<V> {
    private UseCaseAllDictionaries useCase;
    private V view;
    private String[] from = Content.fromAllDictionaries;
    private ArrayList<Map<String,Object>> data;
    private DisposableObserver<Map<String,Object>> getDictionariesListDisposable;
    private Map<String, Object> newDict;
    private ArrayList<Long> idOfDelDicts;
    private Map<String, Object> modifiedDictionary;
    private final String ERROR_MESSAGE_LOADING_DICTIONARY_LIST = "ERROR: load dictionaries";
    private final String ERROR_MESSAGE_ADD_NEW_DICTIONARY = "ERROR: add new dictionary";
    private final String ERROR_MESSAGE_DELETE_ITEMS = "ERROR: delete selected dictionaries";
    private final String ERROR_MESSAGE_EDIT_ITEMS = "ERROR: edit selected dictionary";
    public PresenterAllDictionariesImpl(Context context){
        useCase = new UseCaseAllDictionariesImpl(context);
    }

    @Override
    public void attach(V view) {
        Log.d("LOG_TAG_C/D_Dict", "AllDictionariesPresenterImpl: attach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        this.view = view;
    }

    @Override
    public void detach() {
        Log.d("LOG_TAG_C/D_Dict", "AllDictionariesPresenterImpl: detach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        view = null;
    }

    @Override
    public void destroy() {
        Log.d("LOG_TAG_C/D_Dict", "AllDictionariesPresenterImpl: destroy(): Presenter: " + this.hashCode());
        getDictionariesListDisposable.dispose();
        useCase.destroy();
    }

    @Override
    public void init() {
        Log.d("LOG_TAG", "AllDictionariesPresenterImpl: init()");
        view.setFrom(from);
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
                view.showToast(ERROR_MESSAGE_LOADING_DICTIONARY_LIST);
            }

            @Override
            public void onComplete() {
                view.createAdapter(data);
                view.createDictionariesList();

            }
        });
    }



    @Override
    public void update() {
        Log.d("LOG_TAG", "AllDictionariesPresenterImpl: update()");
        view.createDictionariesList();
    }


    @Override
    public void newDictionary() {
        Log.d("LOG_TAG", "AllDictionariesPresenterImpl: newDictionary()");
        newDict = view.getNewDictionary();
        Single<Map<String,Object>> observable = Single.create(new SingleOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Map<String, Object>> e) throws Exception {
                try {
                    e.onSuccess(newDict);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast(ERROR_MESSAGE_ADD_NEW_DICTIONARY);
                }
            }
        });

        useCase.setNewDictionary(observable);
    }

    @Override
    public void deleteDictionary() {
        Log.d("LOG_TAG", "AllDictionariesPresenterImpl: deleteDictionary()");
        idOfDelDicts = view.getDeletedDictionary();
        Single<ArrayList<Long>> observable = Single.create(new SingleOnSubscribe<ArrayList<Long>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ArrayList<Long>> e) throws Exception {
                try {
                    e.onSuccess(idOfDelDicts);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast(ERROR_MESSAGE_DELETE_ITEMS);
                }
            }
        });
        useCase.deleteDictionaries(observable);
    }

    @Override
    public void editDictionary() {
        Log.d("LOG_TAG", "AllDictionariesPresenterImpl: editDictionary()");
        modifiedDictionary = view.getEditedDictionary();
        Single<Map<String, Object>> observable = Single.create(new SingleOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Map<String, Object>> e) throws Exception {
                try{
                    e.onSuccess(modifiedDictionary);
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast(ERROR_MESSAGE_EDIT_ITEMS);
                }
            }
        });
        useCase.editDictionary(observable);
    }
}
