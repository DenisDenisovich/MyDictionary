package com.dictionary.my.mydictionary.view.dictionary;

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

    AllDictionariesPresenter presenter;
    DictionaryListener mListener;
    ListView listView;
    AllDictionariesAdapter adapter;
    ArrayList<Map<String, Object>> data;
    DialogFragment dialog;
    final int REQUEST_CODE_NEW_DICTIONARY = 1;
    final int REQUEST_CODE_EDIT_DICTIONARY = 2;
    String[] from;
    int[] to = {R.id.dictionary};
    String newDictionary;
    Map<String, Object> modifiedDictionary;
    int selectedItemMod;
    final int CHANGE_MOD = 1;
    final int OPEN_DICTIONARY_MOD = 2;

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
        Log.d("LOG_TAG", "onCreateSUKA");
        presenter.attach(this);


        View view = inflater.inflate(R.layout.all_dictionaries_fragment, null);
        listView = view.findViewById(R.id.lvAllDictionaries);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.allDictionariesFab);
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
    public void createAdapter(ArrayList<Map<String, Object>> data, String... key) {
        this.data = data;
        from = key;
        adapter = new AllDictionariesAdapter(getActivity(),data,R.layout.all_dictionaries_item,from,to);
    }

    @Override
    public void setData(ArrayList<Map<String, Object>> data) {
        this.data = data;
    }


    @Override
    public void createDictionariesList() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectedItemMod == CHANGE_MOD){
                    if(adapter.viewIsDelete(l)){
                        adapter.removeViewFromDeleteList(l);
                    }else{
                        adapter.addViewToDeleteList(l);
                    }
                    //adapter.notifyDataSetChanged();
                    if(adapter.getSizeOfDeleteList() == 0){
                        selectedItemMod = OPEN_DICTIONARY_MOD;
                        mListener.endChangeItemMod();
                    }else{
                        mListener.setDeletedItemCount(adapter.getSizeOfDeleteList());
                    }
                }else if(selectedItemMod == OPEN_DICTIONARY_MOD) {
                    mListener.setDictionary(l);
                    //mListener.openNewDictionary();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectedItemMod != CHANGE_MOD) {
                    adapter.addViewToDeleteList(l);
                    selectedItemMod = CHANGE_MOD;
                    mListener.startChangeItemMod();
                    mListener.setDeletedItemCount(adapter.getSizeOfDeleteList());
                    return true;
                }else{
                    return false;
                }
            }
        });
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
                    mListener.endChangeItemMod();
                    adapter.clearDeleteList();
                    adapter.notifyDataSetChanged();
                    selectedItemMod = OPEN_DICTIONARY_MOD;
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
    public ArrayList<Long> getDeletingDictionary() {
        return adapter.getDeleteList();
    }

    @Override
    public Map<String, Object> getEditingDictionary() {
        return modifiedDictionary;
    }

    @Override
    public int getDeletedItemCount() {
        return adapter.getSizeOfDeleteList();
    }

    @Override
    public void deleteSelectedDictionaries() {
        presenter.deleteDictionary();
        selectedItemMod = OPEN_DICTIONARY_MOD;
    }

    @Override
    public void deleteSelectedDictionariesWithData() {
        if(adapter.getSizeOfDeleteList() == 1){
            presenter.deleteDictionaryWithWords();
            selectedItemMod = OPEN_DICTIONARY_MOD;
        }
    }

    @Override
    public void cancelDeletedDictionaries() {
        adapter.clearDeleteList();
        //selectedItemMod = OPEN_DICTIONARY_MOD;
    }

    @Override
    public void editSelectedDictionary() {
        if(adapter.getSizeOfDeleteList() == 1){
            createEditDictionaryDialog();

        }

    }
    /*
    * TEST CHANGE
    *
    * */
}
