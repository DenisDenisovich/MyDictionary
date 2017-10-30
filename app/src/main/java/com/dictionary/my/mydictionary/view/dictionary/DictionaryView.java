package com.dictionary.my.mydictionary.view.dictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.presenter.dictionary.DictionaryPresenter;
import com.dictionary.my.mydictionary.presenter.dictionary.DictionaryPresenterImpl;
import com.dictionary.my.mydictionary.view.DictionaryListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luxso on 19.09.2017.
 */

public class DictionaryView extends Fragment implements Dictionary, HostToDictionaryCommand {


    DictionaryPresenter presenter;
    private DictionaryListener mListener;
    private ListView listView;
    private AllDictionariesAdapter adapter;
    FloatingActionButton fab;

    private DialogFragment dialog;
    private final int REQUEST_CODE_NEW_WORD = 1;
    private final int REQUEST_CODE_EDIT_WORD = 2;
    private final int REQUEST_CODE_MOVE_WORDS = 3;

    private String[] from;
    private int[] to = {R.id.word_dictionary, R.id.translate_dictionary};

    long initDictionaryId;
    long moveToDictionaryId;
    private Integer sizeOfDeleteList;
    private Map<String, Object> newWord;
    private Map<String, Object> modifiedWord;
    private ArrayList<Long> movedWords;

    private int checkListMod;
    private final int CHANGE_MOD = 1;
    private final int SAY_WORD_MOD = 2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (DictionaryListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new DictionaryPresenterImpl(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attach(this);
        initDictionaryId = mListener.getDictionary();
        View view = inflater.inflate(R.layout.dictionary_fragment,null);
        listView =  view.findViewById(R.id.lvDictionary);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.dictionaryFab);
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
            presenter.init(initDictionaryId);
        }else{
            presenter.update();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void createAdapter(ArrayList<Map<String, Object>> data, String... key) {
        from = key;
        adapter = new AllDictionariesAdapter(getActivity(),data,R.layout.dictionary_item,from,to);
    }

    @Override
    public void createWordsList() {
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
    public Map<String, Object> getNewWord() {
        Map<String, Object> item = new HashMap<>();
        if(newWord != null) {
            item.put(from[1], newWord.get(from[1]));
            item.put(from[2], newWord.get(from[2]));
            newWord = null;
        }
        return item;
    }


    private void createNewWordDialog(){
        dialog = new AddWordDialog();
        dialog.setTargetFragment(this,REQUEST_CODE_NEW_WORD);
        dialog.show(getFragmentManager(),null);
    }

    private void createMoveWordsDialog(){

    }

    private void createEditWordDialog(){

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    break;
                case REQUEST_CODE_EDIT_WORD:
                    break;
            }
        }
    }

    @Override
    public ArrayList<Long> getDeletedWords() {
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
        return null;
    }

    @Override
    public Map<String, Object> getEditedWord() {
        return null;
    }



    @Override
    public Integer getSizeOfDeleteList() {
        return sizeOfDeleteList;
    }

    @Override
    public void deleteSelectedWords() {
        presenter.deleteWords();
    }

    @Override
    public void moveSelectedWords() {
        createMoveWordsDialog();
    }

    @Override
    public void resetCheckList() {
        sizeOfDeleteList = adapter.clearDeleteList();
        checkListMod = SAY_WORD_MOD;
        fab.show();
    }

    @Override
    public void editSelectedDictionary() {
        if(sizeOfDeleteList == 1){
            createEditWordDialog();
        }

    }
}
