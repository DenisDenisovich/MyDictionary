package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterWords;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterWordsImpl;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllWords;
import com.dictionary.my.mydictionary.view.dictionary.adapters.WordAdapter;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.DeleteWordDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.MoveToGroupDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.MoveToTrainingDialog;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

/**
 * This class is used for display words of selected group
 */

public class GroupOfWordsActivity extends AppCompatActivity implements ViewAllWords, DeleteWordDialog.DeleteWordListener,
                                                                                     MoveToGroupDialog.MoveToGroupListener,
                                                                                     MoveToTrainingDialog.MoveToTrainingListener{
    private final static String LOG_TAG = "Log_GroupOfWords";
    private PresenterWords presenter;
    private WordAdapter wordAdapter;

    private String groupId;
    private String groupTitle;
    private Integer countOfSelectedItems;
    private Boolean toolbarSelectedMode = false;
    private TextView toolbarTitle;
    private final static String KEY_TOOLBAR_SELECTED_MODE = "toolbarSelectedMode";
    private DisposableObserver<Integer> selectedItemsObserver;

    private ArrayList<String> movedWords;
    private String moveGroupTitle;
    private final static int REQUEST_CODE_NEW_WORD = 1;
    private final static String KEY_CURRENT_GROUP = "currentGroup";
    private ArrayList<Long> movedWordsToTraining;
    private Integer selectedTrainingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()  " + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_words);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            groupId = intent.getStringExtra(Content.COLUMN_ROWID);
            groupTitle = intent.getStringExtra(Content.COLUMN_TITLE);
            presenter = new PresenterWordsImpl(getApplicationContext(),groupId);
            presenter.attach(this);
            presenter.init();
        }else{
            presenter = (PresenterWords) getLastCustomNonConfigurationInstance();
            presenter.attach(this);
            presenter.update();
            groupId = savedInstanceState.getString(Content.COLUMN_ROWID);
            groupTitle = savedInstanceState.getString(Content.COLUMN_TITLE);
            toolbarSelectedMode = savedInstanceState.getBoolean(KEY_TOOLBAR_SELECTED_MODE);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            toolbarTitle.setText(groupTitle);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        outState.putString(Content.COLUMN_ROWID, groupId);
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
            toolbarTitle.setText(String.valueOf(countOfSelectedItems));
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
            toolbarTitle.setText(groupTitle);
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
                /*case R.id.word_menu_move_to_training:
                    presenter.moveToTrainingSelected();
                    return true;*/
            }
        }else{
            int id = item.getItemId();
            switch (id){
                case android.R.id.home:
                    onBackPressed();
                    return true;
                /*case R.id.word_menu_search:
                    return true;*/
                case R.id.word_menu_add:
                    Intent intent = new Intent(this,AddWordActivity.class);
                    intent.putExtra(KEY_CURRENT_GROUP, groupTitle);
                    startActivityForResult(intent, REQUEST_CODE_NEW_WORD);
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
        Log.d(LOG_TAG, "createDeleteDialog()");
        DialogFragment dialog = DeleteWordDialog.newInstance(wordAdapter.getSelectedItemsSize());
        dialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onDeleteWordPositiveClick() {
        presenter.deleteWordsIsReady();
    }

    @Override
    public ArrayList<String> getDeletedWords() {
        return wordAdapter.getSelectedItemIds();
    }

    @Override
    public void deleteWordsFromList() {
        wordAdapter.deleteSelectedWords();
        toolbarSelectedMode = false;
        invalidateOptionsMenu();
    }

    @Override
    public void createMoveToGroupDialog(ArrayList<Group> groups) {
        Log.d(LOG_TAG, "createMoveToGroupDialog()");
        DialogFragment dialog = MoveToGroupDialog.newInstance(groups);
        dialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public ArrayList<String> getMovedToGroupWords() {
        return movedWords;
    }

    @Override
    public void onMoveToGroupPositiveClick(String groupId, String groupTitle) {
        movedWords = (ArrayList<String>) wordAdapter.getSelectedItemIds().clone();
        moveGroupTitle = groupTitle;
        movedWords.add(0, groupId);
        presenter.movedWordsToGroupIsReady();
    }

    @Override
    public void moveWordsFromList() {
        Toast.makeText(this,"Words are moved to " + moveGroupTitle,Toast.LENGTH_LONG).show();
        wordAdapter.deleteSelectedWords();
        toolbarSelectedMode = false;
        invalidateOptionsMenu();
    }

    @Override
    public void createMoveToTrainingDialog(ArrayList<Long> ids) {
        Log.d(LOG_TAG, "createMoveToGroupDialog()");
        if(wordAdapter.getSelectedItemsSize() <= 20){
            DialogFragment dialog = MoveToTrainingDialog.newInstance(ids);
            dialog.show(getSupportFragmentManager(),null);
        }else{
            showERROR("You can't move to training more than 20 words");
        }
    }

    @Override
    public void onMoveToTrainingPositiveClick(Integer trainingId, String trainingTitle) {
        movedWordsToTraining = (ArrayList<Long>) wordAdapter.getSelectedItemIds().clone();
        selectedTrainingId = trainingId;
        moveGroupTitle = trainingTitle;
        presenter.movedWordsToTrainingIsReady();
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
        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_WORD:
                    Boolean result = data.getBooleanExtra("RESULT", false);
                    if (result) {
                        presenter.init();
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.destroy();
        wordAdapter.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy() " + this.hashCode());
        if(selectedItemsObserver != null) {
            selectedItemsObserver.dispose();
        }
        presenter.saveListState();
        presenter.detach();
    }

}
