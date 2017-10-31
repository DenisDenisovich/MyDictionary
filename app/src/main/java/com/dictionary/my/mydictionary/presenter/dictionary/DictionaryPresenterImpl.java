package com.dictionary.my.mydictionary.presenter.dictionary;

import android.content.Context;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.UseCaseDictionary;
import com.dictionary.my.mydictionary.domain.UseCaseDictionaryImpl;
import com.dictionary.my.mydictionary.view.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 23.09.2017.
 */

public class DictionaryPresenterImpl<V extends Dictionary> implements DictionaryPresenter<V> {
    private V view;
    private UseCaseDictionary useCase;
    private ArrayList<Map<String,Object>> data;
    private Map<String,Object> newWord;
    private ArrayList<Long> idOfDelWords;
    private Map<String, Object> modifiedWord;
    private String[] from = Content.fromDictionary;
    private DisposableObserver<Map<String,Object>> getWordsListDisposable;
    private final String ERROR_MESSAGE_LOADING_WORDS_LIST = "ERROR: load words";
    private final String ERROR_MESSAGE_ADD_NEW_WORD = "ERROR: add new word";
    private final String ERROR_MESSAGE_DELETE_ITEMS = "ERROR: delete selected words";
    private final String ERROR_MESSAGE_EDIT_ITEMS = "ERROR: edit selected word";

    public DictionaryPresenterImpl(Context context, Long currentDictionaryId){
        useCase = new UseCaseDictionaryImpl(context, currentDictionaryId);
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
        getWordsListDisposable.dispose();
        useCase.destroy();
    }

    @Override
    public void init() {
        view.setFrom(from);
        data = new ArrayList<>();
        getWordsListDisposable = useCase.getWordsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Map<String, Object>>() {
                    @Override
                    public void onNext(@NonNull Map<String, Object> stringObjectMap) {
                        data.add(stringObjectMap);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.showToast(ERROR_MESSAGE_LOADING_WORDS_LIST);
                    }

                    @Override
                    public void onComplete() {
                        view.createAdapter(data);
                        view.createWordsList();
                    }
                });
    }

    @Override
    public void update() {
        view.createWordsList();
    }


    @Override
    public void newWord() {
        newWord = view.getNewWord();
        Single<Map<String,Object>> observable = Single.create(new SingleOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Map<String, Object>> e) throws Exception {
                try {
                    e.onSuccess(newWord);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast(ERROR_MESSAGE_ADD_NEW_WORD);
                }
            }
        });

        useCase.setNewWord(observable);
    }

    @Override
    public void deleteWords() {
        idOfDelWords = view.getDeletedWords();
    }

    @Override
    public void moveWords() {

    }

    @Override
    public void editWord() {
        modifiedWord = view.getEditedWord();
    }
}
