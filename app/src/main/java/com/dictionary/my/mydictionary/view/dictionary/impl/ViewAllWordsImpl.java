package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;

/**
 * Created by luxso on 04.03.2018.
 */

public class ViewAllWordsImpl extends Fragment implements ViewAllWords {
    private final static String LOG_TAG = "Log_ViewAllWords: ";
    View myView;
    public interface onAllWordsSelectedListener{
        public void allGroupsScreenSelected();
    }
    private onAllWordsSelectedListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach()");
        try{
            mListener = (onAllWordsSelectedListener)context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");
        myView = inflater.inflate(R.layout.fragment_all_words,null);
        Toolbar toolbar = (Toolbar)myView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);

        if(activity.getSupportActionBar() != null){
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
                    switch (i){
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
        return myView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()");
    }
}
