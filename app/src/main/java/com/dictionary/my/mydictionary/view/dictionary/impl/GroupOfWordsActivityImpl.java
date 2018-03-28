package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAddWord;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterWords;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterWordsImpl;
import com.dictionary.my.mydictionary.view.dictionary.AllWordsFragment;
import com.dictionary.my.mydictionary.view.dictionary.adapters.WordAdapter;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by luxso on 28.03.2018.
 */

public class GroupOfWordsActivityImpl extends AppCompatActivity implements AllWordsFragment {
    private final static String LOG_TAG = "Log_GroupOfWords";
    private PresenterWords presenter;
    private WordAdapter wordAdapter;

    private Long groupId;
    private String groupTitle;
    private Integer countOfSelectedItems;
    private Boolean toolbarSelectedMode = false;
    private final static String KEY_TOOLBAR_SELECTED_MODE = "toolbarSelectedMode";
    private DisposableObserver<Integer> selectedItemsObserver;

    private final static int REQUEST_CODE_MOVE_TO_GROUP = 1;
    private ArrayList<Long> movedWords;
    private final static int REQUEST_CODE_MOVE_TO_TRAINING = 2;
    private final static int REQUEST_CODE_DELETE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()  " + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_words);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            groupId = intent.getLongExtra(Content.COLUMN_ROWID, 0);
            groupTitle = intent.getStringExtra(Content.COLUMN_TITLE);
            presenter = new PresenterWordsImpl(getApplicationContext(),groupId);
            presenter.attach(this);
            presenter.init();
        }else{
            presenter = (PresenterWords) getLastCustomNonConfigurationInstance();
            presenter.attach(this);
            presenter.update();
            groupId = savedInstanceState.getLong(Content.COLUMN_ROWID);
            groupTitle = savedInstanceState.getString(Content.COLUMN_TITLE);
            toolbarSelectedMode = savedInstanceState.getBoolean(KEY_TOOLBAR_SELECTED_MODE);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(groupTitle);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Content.COLUMN_ROWID, groupId);
        outState.putString(Content.COLUMN_TITLE, groupTitle);
        outState.putBoolean(KEY_TOOLBAR_SELECTED_MODE, toolbarSelectedMode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.word_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onPrepareOptionsMenu()");
        if(toolbarSelectedMode){
            menu.setGroupVisible(R.id.word_menu_group_context,true);
            menu.setGroupVisible(R.id.word_menu_group_base,false);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(String.valueOf(countOfSelectedItems));
            }
            if(countOfSelectedItems == 0){
                menu.findItem(R.id.word_menu_move_to_group).setEnabled(false);
                menu.findItem(R.id.word_menu_move_to_training).setEnabled(false);
                menu.findItem(R.id.word_menu_delete).setEnabled(false);
            }else{
                menu.findItem(R.id.word_menu_move_to_group).setEnabled(true);
                menu.findItem(R.id.word_menu_move_to_training).setEnabled(true);
                menu.findItem(R.id.word_menu_delete).setEnabled(true);
            }
        }else{
            menu.setGroupVisible(R.id.word_menu_group_context,false);
            menu.setGroupVisible(R.id.word_menu_group_base,true);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(groupTitle);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected()");
        if(toolbarSelectedMode){
            int id = item.getItemId();
            switch (id){
                case android.R.id.home:
                    if(toolbarSelectedMode) {
                        wordAdapter.selectModeOff();
                        toolbarSelectedMode = false;
                        invalidateOptionsMenu();
                    }
                    return true;
                case R.id.word_menu_delete:
                    presenter.deleteSelected();
                    return true;
                case R.id.word_menu_move_to_group:
                    presenter.moveToGroupSelected();
                    return true;
                case R.id.word_menu_move_to_training:
                    return true;
            }
        }else{
            int id = item.getItemId();
            switch (id){
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.word_menu_search:
                    return true;
                case R.id.word_menu_add:
                    //onNewWordClicked = true;
                    //mListener.showAddWordDialog();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showERROR(String message) {
        Log.d(LOG_TAG, "showERROR()");
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void createList(ArrayList<Word> words) {
        Log.d(LOG_TAG, "createList()");
        RecyclerView rv = findViewById(R.id.rvAllWords);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        wordAdapter = new WordAdapter(this,words);
        rv.setAdapter(wordAdapter);
        subscribeOnRecyclerView();
    }

    private void subscribeOnRecyclerView(){
        selectedItemsObserver = wordAdapter.getSelectedItemsObservable()
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(LOG_TAG, "onNext integer: " + integer);
                        countOfSelectedItems = integer;
                        toolbarSelectedMode = true;
                        invalidateOptionsMenu();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Something was wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void createDeleteDialog() {

    }

    @Override
    public ArrayList<Long> getDeletedWords() {
        return null;
    }

    @Override
    public void deleteWordsFromList() {

    }

    @Override
    public void createMoveToGroupDialog(ArrayList<Group> groups) {

    }

    @Override
    public ArrayList<Long> getMovedToGroupWords() {
        return null;
    }

    @Override
    public void createMoveToTrainingDialog() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy() " + this.hashCode());
        if(selectedItemsObserver != null) {
            selectedItemsObserver.dispose();
        }
        presenter.detach();
    }
}
