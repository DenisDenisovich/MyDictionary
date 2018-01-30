package com.dictionary.my.mydictionary.presenter.dictionary;

import android.content.Context;
import android.util.Log;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.UseCaseDictionary;
import com.dictionary.my.mydictionary.domain.UseCaseDictionaryImpl;
import com.dictionary.my.mydictionary.view.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
    private ArrayList<Map<String,Object>> wordData;
    private ArrayList<Map<String,Object>> dictionaryData;
    private Map<String,Object> newWord;
    private ArrayList<Long> idOfDelWords;
    private Map<String, Object> modifiedWord;
    private ArrayList<Long> movedData;
    private String[] from = Content.fromDictionary;
    private DisposableObserver<Map<String,Object>> getWordsListDisposable;
    private DisposableObserver<Map<String,Object>> getDictionaryListDisposable;
    private final String ERROR_MESSAGE_LOADING_WORDS_LIST = "ERROR: load words";
    private final String ERROR_MESSAGE_ADD_NEW_WORD = "ERROR: add new word";
    private final String ERROR_MESSAGE_DELETE_ITEMS = "ERROR: delete selected words";
    private final String ERROR_MESSAGE_EDIT_ITEMS = "ERROR: edit selected word";
    private final String ERROR_MESSAGE_MOVE_ITEMS = "ERROR: move selected word";

    public DictionaryPresenterImpl(Context context, Long currentDictionaryId){
        useCase = new UseCaseDictionaryImpl(context, currentDictionaryId);
    }
    @Override
    public void attach(V view) {
        Log.d("LOG_TAG_C/D_Dict", "DictionariesPresenterImpl: attach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        this.view = view;
    }

    @Override
    public void detach() {
        Log.d("LOG_TAG_C/D_Dict", "DictionariesPresenterImpl: detach(): Presenter: " + this.hashCode() + " View:" + view.hashCode());
        view = null;
    }

    @Override
    public void destroy() {
        Log.d("LOG_TAG_C/D_Dict", "DictionariesPresenterImpl: destroy(): Presenter: " + this.hashCode());
        if(getWordsListDisposable != null) {
            getWordsListDisposable.dispose();
        }
        if(getDictionaryListDisposable != null){
            getDictionaryListDisposable.dispose();
        }
        useCase.destroy();
    }

    @Override
    public void init() {
        Log.d("LOG_TAG", "DictionaryPresenterImpl: init()");
        view.setFrom(from);
        wordData = new ArrayList<>();
        getWordsListDisposable = useCase.getWordsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Map<String, Object>>() {
                    @Override
                    public void onNext(@NonNull Map<String, Object> stringObjectMap) {
                        wordData.add(stringObjectMap);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.showToast(ERROR_MESSAGE_LOADING_WORDS_LIST);
                    }

                    @Override
                    public void onComplete() {
                        view.createAdapter(wordData);
                        view.createWordsList();
                    }
                });
    }

    @Override
    public void update() {
        Log.d("LOG_TAG", "DictionaryPresenterImpl: update()");
        view.createWordsList();
    }


    @Override
    public void newWord() {
        Log.d("LOG_TAG", "DictionaryPresenterImpl: newWord()");
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
        Log.d("LOG_TAG", "DictionaryPresenterImpl: deleteWords()");
        idOfDelWords = view.getDeletedWords();
        Single<ArrayList<Long>> observable = Single.create(new SingleOnSubscribe<ArrayList<Long>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ArrayList<Long>> e) throws Exception {
                try{
                    e.onSuccess(idOfDelWords);
                    init();
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast(ERROR_MESSAGE_DELETE_ITEMS);
                }
            }
        });
        useCase.deleteWords(observable);
    }

    @Override
    public void getDictionaryList() {
        dictionaryData = new ArrayList<>();
        getDictionaryListDisposable = useCase.getDictionaryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Map<String, Object>>() {
                    @Override
                    public void onNext(@NonNull Map<String, Object> stringObjectMap) {
                        dictionaryData.add(stringObjectMap);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        view.showToast(ERROR_MESSAGE_MOVE_ITEMS);
                    }

                    @Override
                    public void onComplete() {
                        view.setDictionaryList(dictionaryData);
                    }
                });

    }

    @Override
    public void moveWords() {
        Log.d("LOG_TAG", "DictionaryPresenterImpl: moveWords()");
        movedData = view.getMovedWords();
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> e) throws Exception {
                try{
                    for(int i = 0; i < movedData.size(); i++){
                        e.onNext(movedData.get(i));
                    }
                    e.onComplete();
                    init();
                }catch (Throwable t){
                    e.onError(t);
                }
            }
        });
        useCase.moveWords(observable);

    }

    @Override
    public void editWord() {
        Log.d("LOG_TAG", "DictionaryPresenterImpl: editWord()");
        modifiedWord = view.getEditedWord();
        Single<Map<String,Object>> observable = Single.create(new SingleOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Map<String, Object>> e) throws Exception {
                try{
                    e.onSuccess(modifiedWord);
                }catch (Throwable t){
                    e.onError(t);
                    view.showToast(ERROR_MESSAGE_EDIT_ITEMS);
                }
            }
        });
        useCase.editWord(observable);
    }
}
