package com.dictionary.my.mydictionary.view.dictionary;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.dictionary.my.mydictionary.presenter.dictionary.AllDictionariesPresenter;
import com.dictionary.my.mydictionary.presenter.dictionary.AllDictionariesPresenterImpl;
import com.dictionary.my.mydictionary.view.DictionaryListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luxso on 27.09.2017.
 */

public class AllDictionariesView extends Fragment implements AllDictionaries, HostToAllDictionariesCommands {

    private AllDictionariesPresenter presenter;
    private DictionaryListener mListener;
    private ListView listView;
    private AllDictionariesAdapter adapter;
    FloatingActionButton fab;

    private DialogFragment dialog;
    private final int REQUEST_CODE_NEW_DICTIONARY = 1;
    private final int REQUEST_CODE_EDIT_DICTIONARY = 2;

    private String[] from;
    private int[] to = {R.id.dictionary};

    private Integer sizeOfDeleteList;
    private String newDictionary;
    private Map<String, Object> modifiedDictionary;


    private final int CHANGE_MOD = 1;
    private final int OPEN_DICTIONARY_MOD = 2;
    private int checkListMod = OPEN_DICTIONARY_MOD;

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
        presenter = new AllDictionariesPresenterImpl(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attach(this);

        View view = inflater.inflate(R.layout.all_dictionaries_fragment, null);
        listView = view.findViewById(R.id.lvAllDictionaries);
        fab = view.findViewById(R.id.allDictionariesFab);
        if(checkListMod == CHANGE_MOD){
            fab.hide();
        }else{
            fab.show();
        }
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createNewDictionaryDialog();
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
    public void createAdapter(ArrayList<Map<String, Object>> data) {
        adapter = new AllDictionariesAdapter(getActivity(),data,R.layout.all_dictionaries_item,from,to);
    }


    @Override
    public void createDictionariesList() {
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
                        checkListMod = OPEN_DICTIONARY_MOD;
                        mListener.checkListIsChange();
                        fab.show();
                    }else{
                        mListener.checkListIsChange();
                    }
                }else if(checkListMod == OPEN_DICTIONARY_MOD) {
                    Map<String, Object> item = (Map<String, Object>)adapter.getItem(i);
                    String itemString = (String)item.get(from[1]);
                    mListener.selectedDictionary(l,itemString);
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
        this.from = from;
    }

    @Override
    public Map<String, Object> getNewDictionary() {
        Map<String, Object> item = new HashMap<>();
        if(newDictionary != null) {
            item.put(from[1], newDictionary);
            newDictionary = null;
        }
        return item;
    }

    private void createNewDictionaryDialog(){
        dialog = new AddDictionaryDialog();
        dialog.setTargetFragment(this, REQUEST_CODE_NEW_DICTIONARY);
        dialog.show(getFragmentManager(),null);
    }

    private void createEditDictionaryDialog(){
        dialog = new EditDictionaryDialog();
        dialog.setTargetFragment(this, REQUEST_CODE_EDIT_DICTIONARY);
        dialog.show(getFragmentManager(),null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NEW_DICTIONARY:
                    newDictionary = data.getStringExtra(from[1]);
                    presenter.newDictionary();
                    break;
                case REQUEST_CODE_EDIT_DICTIONARY:
                    modifiedDictionary = new HashMap<>();
                    modifiedDictionary.put(from[0],adapter.getDeleteList().get(0));
                    modifiedDictionary.put(from[1],data.getStringExtra(from[1]));
                    presenter.editDictionary();
                    break;
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            switch (requestCode){
                case REQUEST_CODE_NEW_DICTIONARY:
                    break;
                case REQUEST_CODE_EDIT_DICTIONARY:
                    break;
            }
        }
    }

    @Override
    public ArrayList<Long> getDeletedDictionary() {
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
    public Map<String, Object> getEditedDictionary() {
        resetCheckList();
        mListener.checkListIsChange();
        return modifiedDictionary;
    }

    @Override
    public Integer getSizeOfDeleteList() {
        return sizeOfDeleteList;
    }

    @Override
    public void deleteSelectedDictionaries() {
        presenter.deleteDictionary();
    }



    @Override
    public void resetCheckList() {
        sizeOfDeleteList = adapter.clearDeleteList();
        checkListMod = OPEN_DICTIONARY_MOD;
        fab.show();
    }

    @Override
    public void editSelectedDictionary() {
        if(adapter.getSizeOfDeleteList() == 1){
            createEditDictionaryDialog();
        }

    }
}
