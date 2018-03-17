package com.dictionary.my.mydictionary.data.db.dictionary.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.DBHelper;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.data.db.dictionary.DBAllWords;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * This class working only with All_Words table of dataBase
 */

public class DBAllWordsImpl implements DBAllWords {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private DisposableSingleObserver<WordFullInformation> disposableNewWords;
    private DisposableSingleObserver<ArrayList<Long>> disposableDelWords;
    private DisposableSingleObserver<ArrayList<Long>> disposableMoveWords;
    private DisposableSingleObserver<Long> disposableMoveToDictionaryId;
    private DisposableSingleObserver<Word> disposableEditWord;

    public DBAllWordsImpl(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public Single<ArrayList<Word>> getListOfWord() {
        return Single.create(new SingleOnSubscribe<ArrayList<Word>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Word>> e) throws Exception {
                try{
                    String[] column = {Content.COLUMN_ROWID, Content.COLUMN_ENG, Content.COLUMN_RUS, Content.COLUMN_SOUND};
                    Cursor cursor = db.query(Content.TABLE_ALL_WORD,column,null,null,null,null,null);
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
                List<Example> examples = word.getExamples();
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
                        String strPlaceholder = "(";
                        String[] whereArg = new String[longs.size()];
                        for(int i = 0; i < longs.size()-1;i++){
                            strPlaceholder = strPlaceholder.concat("?,");
                            whereArg[i] = longs.get(i).toString();
                        }
                        strPlaceholder = strPlaceholder.concat("?)");
                        whereArg[whereArg.length-1] = longs.get(longs.size()-1).toString();
                        db.delete(Content.TABLE_ALL_WORD,Content.COLUMN_ROWID + " in " + strPlaceholder, whereArg);
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
                    Long moveToDictionaryId;
                    @Override
                    public void onSuccess(ArrayList<Long> longs) {
                        String strPlaceholder = "(";
                        String[] strArg = new String[longs.size()-1];
                        ContentValues cv = new ContentValues();
                        moveToDictionaryId = longs.get(0);
                        for(int i = 1; i < longs.size()-1; i++){
                            strPlaceholder = strPlaceholder.concat("?,");
                            strArg[i-1] = longs.get(i).toString();

                        }
                        strPlaceholder = strPlaceholder.concat("?)");
                        strArg[longs.size()-2] = longs.get(longs.size()-1).toString();
                        cv.put(Content.COLUMN_GROUP_ID, moveToDictionaryId);
                        db.update(Content.TABLE_ALL_WORD, cv, Content.COLUMN_ROWID + " in " + strPlaceholder, strArg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void editWord(Single<Word> observable) {
        disposableEditWord = observable
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Word>() {
                    ContentValues cv = new ContentValues();
                    String idOfModifiedWord;
                    @Override
                    public void onSuccess(@NonNull Word word) {
                        cv.put(Content.COLUMN_RUS, word.getTranslate());
                        idOfModifiedWord = String.valueOf(word.getId());
                        db.update(Content.TABLE_ALL_WORD,cv,Content.COLUMN_ROWID  + " = ?", new String[] {idOfModifiedWord});
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
