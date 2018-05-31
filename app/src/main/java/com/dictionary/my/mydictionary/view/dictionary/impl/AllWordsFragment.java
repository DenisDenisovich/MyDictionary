package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterWords;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterWordsImpl;
import com.dictionary.my.mydictionary.view.OnBottomNavigationClick;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;
import com.dictionary.my.mydictionary.view.dictionary.adapters.SpinnerAdapter;
import com.dictionary.my.mydictionary.view.dictionary.adapters.WordAdapter;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.DeleteWordDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.MoveToGroupDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.MoveToTrainingDialog;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by luxso on 04.03.2018.
 */

public class AllWordsFragment extends Fragment implements ViewAllWords, OnBottomNavigationClick {
    private final static String LOG_TAG = "Log_ViewAllWords";
    private AppCompatActivity activity;
    private View myView;

    private WordAdapter wordAdapter;

    private Integer countOfSelectedItems;
    private Boolean toolbarSelectedMode = false;
    private TextView toolbarTitle;
    private final static String KEY_TOOLBAR_SELECTED_MODE = "toolbarSelectedMode";
    private DisposableObserver<Integer> selectedItemsObserver;

    private PresenterWords presenter;

    private final static int REQUEST_CODE_MOVE_TO_GROUP = 1;
    private ArrayList<String> movedWordsToGroup;
    private String moveGroupTitle;
    private final static int REQUEST_CODE_MOVE_TO_TRAINING = 2;
    private ArrayList<Long> movedWordsToTraining;
    private Integer selectedTrainingId;
    private final static int REQUEST_CODE_DELETE = 3;
    private boolean onNewWordClicked = false;
    private final static String KEY_ON_NEW_WORD_CLICKED = "onNewWordClicked";


    public interface onAllWordsSelectedListener{
        void allGroupsScreenSelected();
        void showAddWordDialog();
        boolean wordIsAdded();
        void showFullInfo(String id);
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
        presenter = new PresenterWordsImpl(getActivity().getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        // use this method, because fragment don't call onCreateView after closing AddWordActivity
        Log.d(LOG_TAG, "onStart()  " + this.hashCode());
        if(onNewWordClicked){
            if(mListener.wordIsAdded()) {
                presenter.init();
            }
            onNewWordClicked = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()  " + this.hashCode());
        presenter.attach(this);
        myView = inflater.inflate(R.layout.fragment_all_words,null);
        Toolbar toolbar = (Toolbar)myView.findViewById(R.id.toolbar);
        activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        toolbarTitle = myView.findViewById(R.id.toolbarTitle);
        if(activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setSpinnerView();

        if(savedInstanceState == null){
            presenter.init();
        }else {
            onNewWordClicked = savedInstanceState.getBoolean(KEY_ON_NEW_WORD_CLICKED);
            toolbarSelectedMode = savedInstanceState.getBoolean(KEY_TOOLBAR_SELECTED_MODE);
            presenter.update();

        }
        return myView;
    }

    private void setSpinnerView(){
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.dictionary_spinner_array));
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

    @Override
    public void createList(ArrayList<Word> words) {
        RecyclerView rv = myView.findViewById(R.id.rvAllWords);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        wordAdapter = new WordAdapter(getActivity(),words);
        rv.setAdapter(wordAdapter);
        subscribeOnRecyclerView();
    }

    @Override
    public ArrayList<String> getSelectedItemIds() {
        return wordAdapter.getSelectedItemIds();
    }

    @Override
    public Boolean getSelectMode() {
        return wordAdapter.getSelectMode();
    }

    @Override
    public void setSelectedItemIds(ArrayList<String> selectedItemIds) {
        wordAdapter.setSelectedItemIds(selectedItemIds);
    }

    @Override
    public void setSelectMode(Boolean selectMode) {
        wordAdapter.setSelectMode(selectMode);
    }

    private void subscribeOnRecyclerView(){
        selectedItemsObserver = wordAdapter.getSelectedItemsObservable()
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        countOfSelectedItems = integer;
                        if(countOfSelectedItems != -1) {
                            toolbarSelectedMode = true;
                            getActivity().invalidateOptionsMenu();
                        }else{
                            String id = wordAdapter.getFullInfoId();
                            Log.d(LOG_TAG, "in Frag " + id);
                            mListener.showFullInfo(id);
                        }
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_ON_NEW_WORD_CLICKED, onNewWordClicked);
        outState.putBoolean(KEY_TOOLBAR_SELECTED_MODE, toolbarSelectedMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionsMenu()");
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
                activity.getSupportActionBar().setDisplayShowCustomEnabled(false);
                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarTitle.setText(String.valueOf(countOfSelectedItems));
            }
            if(countOfSelectedItems == 0){
                menu.findItem(R.id.word_menu_move_to_group).setEnabled(false);
                //menu.findItem(R.id.word_menu_move_to_training).setEnabled(false);
                menu.findItem(R.id.word_menu_delete).setEnabled(false);
            }else{
                menu.findItem(R.id.word_menu_move_to_group).setEnabled(true);
                //menu.findItem(R.id.word_menu_move_to_training).setEnabled(true);
                menu.findItem(R.id.word_menu_delete).setEnabled(true);
            }
        }else{
            menu.setGroupVisible(R.id.word_menu_group_context,false);
            menu.setGroupVisible(R.id.word_menu_group_base,true);
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbarTitle.setVisibility(View.GONE);
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
                    presenter.deleteSelected();
                    return true;
                case R.id.word_menu_move_to_group:
                    presenter.moveToGroupSelected();
                    return true;
                /*case R.id.word_menu_move_to_training:
                    presenter.moveToTrainingSelected();
                    return true;*/
            }
        }else{
            int id = item.getItemId();
            switch (id){
                /*case R.id.word_menu_search:
                    return true;*/
                case R.id.word_menu_add:
                    onNewWordClicked = true;
                    mListener.showAddWordDialog();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createDeleteDialog(){
        Log.d(LOG_TAG, "createDeleteDialog()");
        DialogFragment dialog = DeleteWordDialog.newInstance(wordAdapter.getSelectedItemsSize());
        dialog.setTargetFragment(this, REQUEST_CODE_DELETE);
        dialog.show(getFragmentManager(),null);
    }

    @Override
    public ArrayList<String> getDeletedWords() {
        return wordAdapter.getSelectedItemIds();
    }

    @Override
    public void deleteWordsFromList() {
        wordAdapter.deleteSelectedWords();
        toolbarSelectedMode = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void createMoveToGroupDialog(ArrayList<Group> groups){
        Log.d(LOG_TAG, "createMoveToGroupDialog()");
        DialogFragment dialog = MoveToGroupDialog.newInstance(groups);
        dialog.setTargetFragment(this, REQUEST_CODE_MOVE_TO_GROUP);
        dialog.show(getFragmentManager(),null);
    }

    @Override
    public ArrayList<String> getMovedToGroupWords() {
        return movedWordsToGroup;
    }

    @Override
    public void moveWordsFromList() {
        Toast.makeText(getActivity(),"Words are moved to " + moveGroupTitle,Toast.LENGTH_LONG).show();
        wordAdapter.selectModeOff();
        toolbarSelectedMode = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void createMoveToTrainingDialog(ArrayList<Long> ids){
        Log.d(LOG_TAG, "createMoveToGroupDialog()");
        if(wordAdapter.getSelectedItemsSize() <= 20){
            DialogFragment dialog = MoveToTrainingDialog.newInstance(ids);
            dialog.setTargetFragment(this, REQUEST_CODE_MOVE_TO_TRAINING);
            dialog.show(getFragmentManager(),null);
        }else{
            showERROR("You can't move to training more than 20 words");
        }
    }

    @Override
    public ArrayList<Long> getMovedToTrainingWords() {
        return movedWordsToTraining;
    }

    @Override
    public Integer getSelectedTraining() {
        return selectedTrainingId;
    }

    @Override
    public void allowMoveToTraining() {
        showERROR("Words is moved to " + moveGroupTitle);
        wordAdapter.selectModeOff();
        toolbarSelectedMode = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_DELETE:
                    presenter.deleteWordsIsReady();
                    break;
                case REQUEST_CODE_MOVE_TO_GROUP:
                    movedWordsToGroup = (ArrayList<String>) wordAdapter.getSelectedItemIds().clone();
                    String groupId = data.getStringExtra(Content.COLUMN_ROWID);
                    moveGroupTitle = data.getStringExtra(Content.COLUMN_TITLE);
                    movedWordsToGroup.add(0, groupId);
                    presenter.movedWordsToGroupIsReady();
                    break;
                case REQUEST_CODE_MOVE_TO_TRAINING:
                    movedWordsToTraining = (ArrayList<Long>) wordAdapter.getSelectedItemIds().clone();
                    selectedTrainingId = data.getIntExtra(Content.COLUMN_ROWID,1);
                    moveGroupTitle = data.getStringExtra(Content.COLUMN_TRAININGS);
                    presenter.movedWordsToTrainingIsReady();
                    break;
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            switch (requestCode){
                case REQUEST_CODE_DELETE:
                    break;
                case REQUEST_CODE_MOVE_TO_GROUP:
                    break;
                case REQUEST_CODE_MOVE_TO_TRAINING:
                    break;
            }
        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showERROR(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateView() {
        presenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView() " + this.hashCode());
        if(selectedItemsObserver != null) {
            selectedItemsObserver.dispose();
        }
        presenter.saveListState();
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()     " + this.hashCode());
        presenter.destroy();
        wordAdapter.destroy();
    }
}
