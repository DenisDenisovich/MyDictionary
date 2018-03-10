package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.entites.Word;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;
import com.dictionary.my.mydictionary.view.dictionary.adapters.WordAdapter;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by luxso on 04.03.2018.
 */

public class ViewAllWordsImpl extends Fragment implements ViewAllWords {
    private final static String LOG_TAG = "Log_ViewAllWords";
    private AppCompatActivity activity;
    private View myView;
    private WordAdapter wordAdapter;
    private Integer countOfSelectedItems;
    private Boolean toolbarSelectedMode = false;
    private DisposableObserver<Integer> selectedItemsObserver;

    private ArrayList<Word> data;

    public interface onAllWordsSelectedListener{
        public void allGroupsScreenSelected();
    }
    private onAllWordsSelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach()      " + this.hashCode());
        try{
            mListener = (onAllWordsSelectedListener)context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()      " + this.hashCode());
        setRetainInstance(true);
        setHasOptionsMenu(true);
        data = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            Word item = new Word();
            item.setId(i);
            item.setWord("Word " + String.valueOf(i));
            item.setTranslate("Translate " + String.valueOf(i));
            data.add(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()  " + this.hashCode());
        myView = inflater.inflate(R.layout.fragment_all_words,null);
        Toolbar toolbar = (Toolbar)myView.findViewById(R.id.toolbar);
        activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        setSpinnerView();
        RecyclerView rv = myView.findViewById(R.id.rvAllWords);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        wordAdapter = new WordAdapter(getActivity(),data);
        rv.setAdapter(wordAdapter);
        subscribeOnRecyclerView();
        return myView;
    }

    private void setSpinnerView(){
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.dictionary_spinner_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
            Spinner spinner = new Spinner(activity.getSupportActionBar().getThemedContext());
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            activity.getSupportActionBar().setCustomView(spinner);
            activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 1:
                            mListener.allGroupsScreenSelected();
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    private void subscribeOnRecyclerView(){
        selectedItemsObserver = wordAdapter.getSelectedItemsObservable()
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(LOG_TAG, "onNext integer: " + integer);
                        countOfSelectedItems = integer;
                        toolbarSelectedMode = true;
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Something was wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionasMenu()");
        inflater.inflate(R.menu.word_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onPrepareOptionsMenu()");
        if(toolbarSelectedMode){
            menu.setGroupVisible(R.id.word_menu_group_context,true);
            menu.setGroupVisible(R.id.word_menu_group_base,false);
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
                activity.getSupportActionBar().setDisplayShowCustomEnabled(false);
                activity.setTitle(String.valueOf(countOfSelectedItems));
            }
            if(countOfSelectedItems == 0){
                menu.findItem(R.id.word_menu_move).setEnabled(false);
                menu.findItem(R.id.word_menu_delete).setEnabled(false);
            }else{
                menu.findItem(R.id.word_menu_move).setEnabled(true);
                menu.findItem(R.id.word_menu_delete).setEnabled(true);
            }
            if(countOfSelectedItems == 1){
                menu.findItem(R.id.word_menu_edit).setEnabled(true);
            }else{
                menu.findItem(R.id.word_menu_edit).setEnabled(false);
            }
        }else{
            menu.setGroupVisible(R.id.word_menu_group_context,false);
            menu.setGroupVisible(R.id.word_menu_group_base,true);
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected()");
        if(toolbarSelectedMode){
            int id = item.getItemId();
            switch (id){
                case android.R.id.home:
                    wordAdapter.selectModeOff();
                    toolbarSelectedMode = false;
                    getActivity().invalidateOptionsMenu();
                    return true;
                case R.id.word_menu_delete:
                    Toast.makeText(getActivity(), "delete", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.word_menu_edit:
                    Toast.makeText(getActivity(), "edit", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.word_menu_move:
                    Toast.makeText(getActivity(), "move", Toast.LENGTH_LONG).show();
                    return true;
            }
        }else{
            int id = item.getItemId();
            switch (id){
                case R.id.word_menu_search:
                    Toast.makeText(getActivity(), "search", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.word_menu_add:
                    Toast.makeText(getActivity(), "add", Toast.LENGTH_LONG).show();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView() " + this.hashCode());
        if(selectedItemsObserver != null) {
            selectedItemsObserver.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()     " + this.hashCode());
    }
}
