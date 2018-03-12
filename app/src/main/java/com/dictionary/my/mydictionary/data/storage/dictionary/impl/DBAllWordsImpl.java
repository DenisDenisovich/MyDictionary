package com.dictionary.my.mydictionary.data.storage.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.data.entites.dictionary.Word;
import com.dictionary.my.mydictionary.data.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.entites.skyengapi.meaning.Example;
import com.dictionary.my.mydictionary.data.storage.dictionary.DBAllWords;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luxso on 23.09.2017.
 */

public class DBAllWordsImpl implements DBAllWords {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Long currentDictionaryId;
    Long moveToDictionaryId;
    DisposableSingleObserver<WordFullInformation> disposableNewWords;
    DisposableSingleObserver<ArrayList<Long>> disposableDelWords;
    DisposableSingleObserver<ArrayList<Long>> disposableMoveWords;
    DisposableSingleObserver<Long> disposableMoveToDictionaryId;
    DisposableSingleObserver<Word> disposableEditWord;

    public DBAllWordsImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public Single<ArrayList<Word>> getListOfWord() {
        return Single.create(new SingleOnSubscribe<ArrayList<Word>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Word>> e) throws Exception {
                String[] column = {Content.COLUMN_ROWID, Content.COLUMN_ENG, Content.COLUMN_RUS, Content.COLUMN_SOUND};
                Cursor cursor = db.query(Content.TABLE_ALL_WORD,column,null,null,null,null,null);
                try{
                    if(cursor.moveToFirst()){
                        ArrayList<Word> list = new ArrayList<>();
                        do{
                            Word item = new Word();
                            item.setId(cursor.getInt(cursor.getColumnIndex(Content.COLUMN_ROWID)));
                            item.setWord(cursor.getString(cursor.getColumnIndex(Content.COLUMN_ENG)));
                            item.setTranslate(cursor.getString(cursor.getColumnIndex(Content.COLUMN_RUS)));
                            item.setSound(cursor.getString(cursor.getColumnIndex(Content.COLUMN_SOUND)));
                            list.add(item);
                        }while (cursor.moveToNext());
                        if(!e.isDisposed()){
                            e.onSuccess(list);
                        }
                    }
                }catch (Throwable t){
                    if(!e.isDisposed()) {
                        e.onError(t);
                    }
                }
            }
        });
    }

    @Override
    public void setNewWord(Single<WordFullInformation> observable) {
        disposableNewWords = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<WordFullInformation>() {
            @Override
            public void onSuccess(@NonNull WordFullInformation word) {
                ContentValues cv = new ContentValues();
                cv.put(Content.COLUMN_ENG, word.getEng());
                cv.put(Content.COLUMN_RUS, word.getRus());
                cv.put(Content.COLUMN_NOTE, word.getNote());
                cv.put(Content.COLUMN_TRANSCRIPTION, word.getTranscription());
                cv.put(Content.COLUMN_SOUND, word.getSound());
                cv.put(Content.COLUMN_PART_OF_SPEECH, word.getPartOfSpeech());
                cv.put(Content.COLUMN_PREVIEW_IMAGE, word.getPreviewImage());
                cv.put(Content.COLUMN_IMAGE, word.getImage());
                cv.put(Content.COLUMN_DEFINITION, word.getDefinition());
                cv.put(Content.COLUMN_DATE, word.getDate());
                cv.put(Content.COLUMN_GROUP_ID, word.getGroupId());

                String str = "";
                ArrayList<Example> examples = word.getExamples();
                for(int i = 0; i < examples.size(); i++){
                    str = str.concat(examples.get(i).getText());
                    if(i < (examples.size() -1)){
                        str = str.concat(Content.ARRAY_SEPARATOR);
                    }
                }
                cv.put(Content.COLUMN_EXAMPLES, str);
                str = "";
                ArrayList<String> alternative = word.getAlternative();
                for(int i = 0; i < alternative.size(); i++){
                    str = str.concat(alternative.get(i));
                    if(i < (alternative.size() -1)){
                        str = str.concat(Content.ARRAY_SEPARATOR);
                    }
                }
                cv.put(Content.COLUMN_ALTERNATIVE, str);
                db.insert(Content.TABLE_ALL_WORD,null,cv);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void deleteWords(Single<ArrayList<Long>> observable) {
        disposableDelWords = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Long>>() {
                    @Override
                    public void onSuccess(@NonNull ArrayList<Long> longs) {
                        String[] whereArg = new String[longs.size()];
                        for(int i = 0; i < longs.size();i++){
                            whereArg[i] = longs.get(i).toString();
                            //db.delete(Content.TABLE_ALL_WORD,Content.deleteDbDictionary + whereArg[i], null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void moveWords(Single<ArrayList<Long>> observable) {
        disposableMoveWords = observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ArrayList<Long>>() {
                    @Override
                    public void onSuccess(ArrayList<Long> longs) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    @Override
    public void editWord(Single<Word> observable) {
        disposableEditWord = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Word>() {
                    ContentValues cv = new ContentValues();
                    Long idOfModifiedWord;
                    @Override
                    public void onSuccess(@NonNull Word stringObjectMap) {
                        //idOfModifiedWord = (Long)stringObjectMap.get(Content.fromDictionary[0]);
                        //cv = MapToContentValuesDictionary.map(stringObjectMap);
                        //db.update(Content.TABLE_ALL_WORD,cv,Content.editDbDictionary + idOfModifiedWord,null);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                       e.printStackTrace();
                    }
                });
    }

    @Override
    public void destroy() {
        if(disposableNewWords != null) {
            disposableNewWords.dispose();
        }
        if(disposableDelWords != null) {
            disposableDelWords.dispose();
        }
        if(disposableEditWord != null){
            disposableEditWord.dispose();
        }
        if(disposableMoveWords != null){
            disposableMoveWords.dispose();
        }
        if(disposableMoveToDictionaryId != null){
            disposableMoveToDictionaryId.dispose();
        }
        db.close();
    }
}
