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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAllGroups;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterAllGroupsImpl;
import com.dictionary.my.mydictionary.view.dictionary.ViewAllGroups;
import com.dictionary.my.mydictionary.view.dictionary.adapters.GroupAdapter;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.DeleteGroupDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.EditGroupDialog;
import com.dictionary.my.mydictionary.view.dictionary.dialogs.NewGroupDialog;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by luxso on 07.03.2018.
 */

public class ViewAllGroupsImpl extends Fragment implements ViewAllGroups {
    private final static String LOG_TAG = "Log_ViewAllGroups";
    private AppCompatActivity activity;
    private View myView;
    private GroupAdapter groupAdapter;

    private Integer countOfSelectedItems;
    private Boolean toolbarSelectedMode = false;
    private final static String KEY_TOOLBAR_SELECTED_MODE = "toolbarSelectedMode";
    private DisposableObserver<Integer> countOfSelectObserver;
    private DisposableObserver<Long> choiceGroupObservable;

    private PresenterAllGroups presenter;

    private final static int REQUEST_CODE_DELETE_GROUP = 1;
    private final static int REQUEST_CODE_EDIT_GROUP = 2;
    private final static int REQUEST_CODE_NEW_GROUP = 3;
    Group newGroup;
    Group editGroup;
    public interface onAllGroupsSelectedListener{
        void allWordsScreenSelected();
        void groupOfWordsSelected(long groupId);
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
        presenter = new PresenterAllGroupsImpl(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()  " + this.hashCode());
        presenter.attach(this);
        myView = inflater.inflate(R.layout.fragment_all_groups,null);
        Toolbar toolbar = (Toolbar)myView.findViewById(R.id.toolbar);
        activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        setSpinnerView();

        if(savedInstanceState == null){
            presenter.init();
        }else {
            toolbarSelectedMode = savedInstanceState.getBoolean(KEY_TOOLBAR_SELECTED_MODE);
            presenter.update();

        }

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
    public void createList(ArrayList<Group> words) {
        RecyclerView rv = myView.findViewById(R.id.rvAllGroups);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        groupAdapter = new GroupAdapter(getActivity(),words);
        rv.setAdapter(groupAdapter);
        subscribeOnRecyclerView();
    }

    private void subscribeOnRecyclerView(){
        countOfSelectObserver = groupAdapter.getcountOfSelectObservable()
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
        choiceGroupObservable = groupAdapter.getChoiceGroupObservable()
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mListener.groupOfWordsSelected(aLong);
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
        outState.putBoolean(KEY_TOOLBAR_SELECTED_MODE, toolbarSelectedMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionasMenu()");
        inflater.inflate(R.menu.grop_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onPrepareOptionsMenu()");
        if(toolbarSelectedMode){
            menu.setGroupVisible(R.id.group_menu_group_context,true);
            menu.setGroupVisible(R.id.group_menu_group_base,false);
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
                activity.getSupportActionBar().setDisplayShowCustomEnabled(false);
                activity.setTitle(String.valueOf(countOfSelectedItems));
            }
            if(countOfSelectedItems == 0){
                menu.findItem(R.id.group_menu_edit).setEnabled(false);
                menu.findItem(R.id.group_menu_delete).setEnabled(false);
            }else if(countOfSelectedItems == 1){
                menu.findItem(R.id.group_menu_edit).setEnabled(true);
                menu.findItem(R.id.group_menu_delete).setEnabled(true);
            }else{
                menu.findItem(R.id.group_menu_edit).setEnabled(false);
                menu.findItem(R.id.group_menu_delete).setEnabled(true);
            }
        }else{
            menu.setGroupVisible(R.id.group_menu_group_context,false);
            menu.setGroupVisible(R.id.group_menu_group_base,true);
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
                    groupAdapter.selectModeOff();
                    toolbarSelectedMode = false;
                    getActivity().invalidateOptionsMenu();
                    return true;
                case R.id.group_menu_delete:
                    presenter.deleteSelected();
                    return true;
                case R.id.group_menu_edit:
                    presenter.editSelected();
                    return true;
            }
        }else{
            int id = item.getItemId();
            switch (id){
                case R.id.group_menu_add:
                    presenter.newSelected();
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
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void createNewGroupDialog() {
        Log.d(LOG_TAG, "createNewGroupDialog()");
        DialogFragment dialog = new NewGroupDialog();
        dialog.setTargetFragment(this, REQUEST_CODE_NEW_GROUP);
        dialog.show(getFragmentManager(),null);
    }

    @Override
    public Group getNewGroup() {
        return newGroup;
    }

    @Override
    public void createDeleteDialog() {
        Log.d(LOG_TAG, "createDeleteDialog()");
        DialogFragment dialog = DeleteGroupDialog.newInstance(groupAdapter.getSelectedItemsSize());
        dialog.setTargetFragment(this, REQUEST_CODE_DELETE_GROUP);
        dialog.show(getFragmentManager(),null);
    }

    @Override
    public ArrayList<Long> getDeletedGroups() {
        return groupAdapter.getSelectedItemIds();
    }

    @Override
    public void deleteGroupFromList() {
        groupAdapter.deleteSelectedGroups();
        toolbarSelectedMode = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void createEditDialog() {
        Log.d(LOG_TAG, "createEditDialog()");
        editGroup = groupAdapter.getSelectedGroup();
        String title = editGroup.getTitle();
        DialogFragment dialog = EditGroupDialog.newInstance(title);
        dialog.setTargetFragment(this, REQUEST_CODE_EDIT_GROUP);
        dialog.show(getFragmentManager(),null);

    }

    @Override
    public Group getEditedGroup() {
        return editGroup;
    }

    @Override
    public void editGroupInList() {
        groupAdapter.editSelectedGroup(editGroup);
        toolbarSelectedMode = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_DELETE_GROUP:
                    presenter.deleteGroupsIsReady();
                    break;
                case REQUEST_CODE_EDIT_GROUP:
                    String newTitle = data.getStringExtra(Content.COLUMN_TITLE);
                    editGroup.setTitle(newTitle);
                    presenter.editGroupIsReady();
                    break;
                case REQUEST_CODE_NEW_GROUP:
                    newGroup = new Group();
                    String title = data.getStringExtra(Content.COLUMN_TITLE);
                    newGroup.setTitle(title);
                    presenter.newGroupIsReady();
                    break;
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            switch (requestCode){
                case REQUEST_CODE_DELETE_GROUP:
                    break;
                case REQUEST_CODE_EDIT_GROUP:
                    break;
                case REQUEST_CODE_NEW_GROUP:
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView() " + this.hashCode());
        if(countOfSelectObserver != null) {
            countOfSelectObserver.dispose();
        }
        if(choiceGroupObservable != null){
            choiceGroupObservable.dispose();
        }
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()     " + this.hashCode());
        presenter.destroy();
    }
}
