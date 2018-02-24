package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterDictionary;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterDictionaryImpl;
import com.dictionary.my.mydictionary.view.DictionaryListener;
import com.dictionary.my.mydictionary.view.dictionary.ViewDictionary;
import com.dictionary.my.mydictionary.view.dictionary.HostToDictionaryCommand;
import com.dictionary.my.mydictionary.view.dictionary.adapters.WordAdapter;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.AddWordDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.DeleteWordDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.EditWordDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.MoveWordDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luxso on 19.09.2017.
 */

public class ViewDictionaryImpl extends Fragment implements ViewDictionary, HostToDictionaryCommand {


    PresenterDictionary presenter;
    private DictionaryListener mListener;
    private ListView listView;
    private WordAdapter adapter;
    FloatingActionButton fab;

    private DialogFragment dialog;
    private final int REQUEST_CODE_NEW_WORD = 1;
    private final int REQUEST_CODE_EDIT_WORD = 2;
    private final int REQUEST_CODE_MOVE_WORDS = 3;
    private final int REQUEST_CODE_DELETE_WORDS = 4;

    private String[] from = Content.fromDictionary;
    private int[] to = {R.id.word_dictionary, R.id.translate_dictionary};

    long currentDictionaryId;
    long moveToDictionaryId;
    private Integer sizeOfDeleteList = 0;
    private Map<String, Object> newWord;
    private Map<String, Object> modifiedWord = new HashMap<>();
    private ArrayList<Long> movedWords;
    private ArrayList<Map<String,Object>> dictionaryList;

    private int checkListMod;
    private final int CHANGE_MOD = 1;
    private final int SAY_WORD_MOD = 2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            Log.d("LOG_TAG", "DictionaryView: onAttach()");
            mListener = (DictionaryListener) context;
            currentDictionaryId = mListener.getDictionary();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG_C/D_Dict", "DictionaryView: onCreate() " + this.hashCode());
        setRetainInstance(true);
        presenter = new PresenterDictionaryImpl(getActivity().getApplicationContext(), currentDictionaryId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attach(this);
        Log.d("LOG_TAG_C/D_Dict", "DictionaryView: onCreateView() " + this.hashCode());
        View view = inflater.inflate(R.layout.dictionary_fragment,null);
        listView =  view.findViewById(R.id.lvDictionary);
        fab = (FloatingActionButton) view.findViewById(R.id.dictionaryFab);
        if(checkListMod == CHANGE_MOD){
            fab.hide();
        }else{
            fab.show();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewWordDialog();
            }
        });
        if(savedInstanceState == null){
            presenter.init();
        }else{
            presenter.update();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG_TAG_C/D_Dict", "DictionaryView: onDestroyView()" + this.hashCode());
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG_C/D_Dict", "DictionaryView: onDestroy() " + this.hashCode());
        presenter.destroy();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void createAdapter(ArrayList<Map<String, Object>> data) {
        Log.d("LOG_TAG", "DictionaryView: createAdapter()");
        adapter = new WordAdapter(getActivity(),data,R.layout.word_item,from,to);
        sizeOfDeleteList = 0;
        mListener.checkListIsChange();
    }

    @Override
    public void createWordsList() {
        Log.d("LOG_TAG", "DictionaryView: createDictionariesList()");
        listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(checkListMod == CHANGE_MOD){
                if(adapter.viewIsDelete(l)){
                    sizeOfDeleteList = adapter.removeViewFromDeleteList(l);
                }else{
                    sizeOfDeleteList = adapter.addViewToDeleteList(l);
                }
                if(sizeOfDeleteList == 0){
                    checkListMod = SAY_WORD_MOD;
                    mListener.checkListIsChange();
                    fab.show();
                }else{
                    mListener.checkListIsChange();
                }
            }else if(checkListMod == SAY_WORD_MOD) {

            }
        }
    });
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(checkListMod != CHANGE_MOD) {
                sizeOfDeleteList = adapter.addViewToDeleteList(l);
                checkListMod = CHANGE_MOD;
                mListener.checkListIsChange();
                fab.hide();
                return true;
            }else{
                return false;
            }
        }
    });
    }

    @Override
    public void setFrom(String... from) {
        Log.d("LOG_TAG", "DictionaryView: setFrom()");
        this.from = from;
    }

    @Override
    public void setDictionaryList(ArrayList<Map<String, Object>> dictionaryData) {
        this.dictionaryList = dictionaryData;
        createMoveWordsDialog();
    }


    @Override
    public Map<String, Object> getNewWord() {
        Log.d("LOG_TAG", "DictionaryView: getNewWord()");
        Map<String, Object> item = new HashMap<>();
        if(newWord != null) {
            item.put(from[1], newWord.get(from[1]));
            item.put(from[2], newWord.get(from[2]));
            newWord = null;
        }
        return item;
    }


    private void createNewWordDialog(){
        Log.d("LOG_TAG", "DictionaryView: createNewWordDialog()");
        dialog = new AddWordDialog();
        dialog.setTargetFragment(this,REQUEST_CODE_NEW_WORD);
        dialog.show(getFragmentManager(),null);
    }

    private void createMoveWordsDialog(){
        Log.d("LOG_TAG", "DictionaryView: createMoveWordsDialog()");
        dialog = MoveWordDialog.newInstance(dictionaryList);
        dialog.setTargetFragment(this, REQUEST_CODE_MOVE_WORDS);
        dialog.show(getFragmentManager(),null);

    }

    private void createEditWordDialog(){
        Log.d("LOG_TAG", "DictionaryView: createEditWordDialog()");
        modifiedWord = adapter.getEditItem();
        String itemTranslate = (String)modifiedWord.get(from[2]);
        dialog = EditWordDialog.newInstance(itemTranslate);
        dialog.setTargetFragment(this, REQUEST_CODE_EDIT_WORD);
        dialog.show(getFragmentManager(),null);
    }
    private void createDeleteWordDialog(){
        Log.d("LOG_TAG", "DictionaryView: createDeleteWordDialog()");
        dialog = DeleteWordDialog.newInstance(getSizeOfDeleteList());
        dialog.setTargetFragment(this,REQUEST_CODE_DELETE_WORDS);
        dialog.show(getFragmentManager(),null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LOG_TAG", "DictionaryView: onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NEW_WORD:
                    newWord = new HashMap<>();
                    newWord.put(from[1],data.getStringExtra(from[1]));
                    newWord.put(from[2],data.getStringExtra(from[2]));
                    presenter.newWord();
                    break;
                case REQUEST_CODE_MOVE_WORDS:
                    movedWords = new ArrayList<>();
                    moveToDictionaryId = (data.getLongExtra(from[0],currentDictionaryId));
                    movedWords.add(moveToDictionaryId);
                    ArrayList<Long> moveW = adapter.getDeleteList();
                    for(int i = 0; i < adapter.getSizeOfDeleteList(); i++){
                        movedWords.add(moveW.get(i));
                    }
                    presenter.moveWords();
                    break;
                case REQUEST_CODE_EDIT_WORD:
                    modifiedWord.put(from[2],data.getStringExtra(from[2]));
                    presenter.editWord();
                    break;
                case REQUEST_CODE_DELETE_WORDS:
                    presenter.deleteWords();
                    break;
            }
        }
    }

    @Override
    public ArrayList<Long> getDeletedWords() {
        Log.d("LOG_TAG", "DictionaryView: getDeletedWords()");
        ArrayList<Long> list = new ArrayList<>();
        ArrayList<Long> buf = adapter.getDeleteList();
        for(int i = 0; i < adapter.getSizeOfDeleteList(); i++){
            list.add(buf.get(i));
        }
        resetCheckList();
        mListener.checkListIsChange();
        return list;
    }

    @Override
    public ArrayList<Long> getMovedWords() {
        Log.d("LOG_TAG", "DictionaryView: getMovedWords()");
        return movedWords;
    }

    @Override
    public Map<String, Object> getEditedWord() {
        Log.d("LOG_TAG", "DictionaryView: getEditedWord()");
        resetCheckList();
        mListener.checkListIsChange();
        return modifiedWord;
    }



    @Override
    public Integer getSizeOfDeleteList() {
        Log.d("LOG_TAG", "DictionaryView: getSizeOfDeleteList()");
        return sizeOfDeleteList;
    }

    @Override
    public void deleteSelectedWords() {
        Log.d("LOG_TAG", "DictionaryView: deleteSelectedWords()");
        createDeleteWordDialog();

        //presenter.deleteWords();
    }

    @Override
    public void moveSelectedWords() {
        Log.d("LOG_TAG", "DictionaryView: moveSelectedWords()");
        presenter.getDictionaryList();
    }

    @Override
    public void resetCheckList() {
        Log.d("LOG_TAG", "DictionaryView: resetCheckList()");
        sizeOfDeleteList = adapter.clearDeleteList();
        checkListMod = SAY_WORD_MOD;
        fab.show();
    }

    @Override
    public void editSelectedDictionary() {
        Log.d("LOG_TAG", "DictionaryView: editSelectedDictionary()");
        if(sizeOfDeleteList == 1){
            createEditWordDialog();
        }

    }
}
