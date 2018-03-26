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
import android.widget.Spinner;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllGroups;

import java.util.ArrayList;

/**
 * Created by luxso on 07.03.2018.
 */

public class ViewAllGroupsImpl extends Fragment implements ViewAllGroups {
    private final static String LOG_TAG = "Log_ViewAllGroups";
    private AppCompatActivity activity;
    private View myView;

    public interface onAllGroupsSelectedListener{
        public void allWordsScreenSelected();
        public void groupOfWordsSelected(long groupId);
    }
    private onAllGroupsSelectedListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach()      " + this.hashCode());
        try {
            mListener = (onAllGroupsSelectedListener)context;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()  " + this.hashCode());
        myView = inflater.inflate(R.layout.fragment_all_groups,null);
        Toolbar toolbar = (Toolbar)myView.findViewById(R.id.toolbar);
        activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        setSpinnerView();

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
            spinner.setSelection(1);
            activity.getSupportActionBar().setCustomView(spinner);
            activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            mListener.allWordsScreenSelected();
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
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
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showERROR(String message) {

    }

    @Override
    public void createList(ArrayList<Group> words) {

    }

    @Override
    public void createNewGroupDialog() {

    }

    @Override
    public Group getNewGroup() {
        return null;
    }

    @Override
    public void createDeleteDialog() {

    }

    @Override
    public ArrayList<Long> getDeletedGroups() {
        return null;
    }

    @Override
    public void deleteGroupFromList() {

    }

    @Override
    public void createEditDialog() {

    }

    @Override
    public Group getEditedGroup() {
        return null;
    }

    @Override
    public void editGroupInList() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView() " + this.hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()     " + this.hashCode());
    }
}
