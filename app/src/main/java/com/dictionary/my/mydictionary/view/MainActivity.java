package com.dictionary.my.mydictionary.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.view.dictionary.impl.AddWordActivity;
import com.dictionary.my.mydictionary.view.dictionary.impl.AllGroupsFragment;
import com.dictionary.my.mydictionary.view.dictionary.impl.AllWordsFragment;
import com.dictionary.my.mydictionary.view.dictionary.impl.GroupOfWordsActivity;
import com.dictionary.my.mydictionary.view.trainings.impl.AllTrainingsFragment;
import com.dictionary.my.mydictionary.view.trainings.impl.ConstructorTrainingActivity;
import com.dictionary.my.mydictionary.view.trainings.impl.EngRusTrainingActivity;
import com.dictionary.my.mydictionary.view.trainings.impl.RusEngTrainingActivity;
import com.dictionary.my.mydictionary.view.trainings.impl.SprintTrainingActivity;

public class MainActivity extends AppCompatActivity implements AllWordsFragment.onAllWordsSelectedListener,
                                                               AllGroupsFragment.onAllGroupsSelectedListener,
                                                               AllTrainingsFragment.onTrainingSelectedListener{
    private final int REQUEST_CODE_NEW_WORD = 1;
    private Boolean addWordResult = false;

    private final static String LOG_TAG = "Log_ActivityMain";
    private final static String KEY_ALL_WORDS = "allWordsFragment";
    private final static String KEY_ALL_GROUPS = "allGroupsFragment";
    private final static String KEY_TRAININGS = "trainingsFragment";
    private final static String KEY_NOTEPAD = "notepadFragment";
    private AllWordsFragment allWordsFragment;
    private AllGroupsFragment allGroupsFragment;
    private OnBottomNavigationClick updateFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dictionary:
                    if(getSupportFragmentManager().findFragmentByTag(KEY_ALL_WORDS) != null && getSupportFragmentManager().findFragmentByTag(KEY_ALL_WORDS).isVisible()){
                        updateFragment.updateView();
                    }else if(getSupportFragmentManager().findFragmentByTag(KEY_ALL_GROUPS) != null && getSupportFragmentManager().findFragmentByTag(KEY_ALL_GROUPS).isVisible()){
                        updateFragment.updateView();
                    }else {
                        allWordsScreenSelected();
                    }

                    return true;
                case R.id.navigation_trainings:
                    if(getSupportFragmentManager().findFragmentByTag(KEY_TRAININGS) == null){
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainActivityContainer,new AllTrainingsFragment(),KEY_TRAININGS);
                        ft.commit();
                    }
                    return true;
                case R.id.navigation_notepad:
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate()");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null){
            allWordsScreenSelected();
        }
    }


    @Override
    public void allWordsScreenSelected() {
        allWordsFragment = new AllWordsFragment();
        updateFragment = allWordsFragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivityContainer,allWordsFragment,KEY_ALL_WORDS);
        ft.commit();
    }

    @Override
    public void groupOfWordsSelected(String groupId, String title) {
        Intent intent = new Intent(this,GroupOfWordsActivity.class);
        intent.putExtra(Content.COLUMN_ROWID, groupId);
        intent.putExtra(Content.COLUMN_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void allGroupsScreenSelected() {
        allGroupsFragment = new AllGroupsFragment();
        updateFragment = allGroupsFragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivityContainer,allGroupsFragment,KEY_ALL_GROUPS);
        ft.commit();
    }

    @Override
    public void showAddWordDialog() {
        Intent intent = new Intent(this,AddWordActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_WORD);
    }

    @Override
    public boolean wordIsAdded() {
        return addWordResult;
    }

    @Override
    public void showEngRusTraining() {
        Log.d(LOG_TAG, "showEngRusTraining()");
        Intent intent = new Intent(this, EngRusTrainingActivity.class);
        startActivity(intent);
    }

    @Override
    public void showRusEngTraining() {
        Log.d(LOG_TAG, "showRusEngTraining()");
        Intent intent = new Intent(this, RusEngTrainingActivity.class);
        startActivity(intent);
    }

    @Override
    public void showConstructorTraining() {
        Log.d(LOG_TAG, "showConstructorTraining()");
        Intent intent = new Intent(this, ConstructorTrainingActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSprintTraining() {
        Log.d(LOG_TAG, "showSprintTraining()");
        Intent intent = new Intent(this, SprintTrainingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NEW_WORD:
                    addWordResult = data.getBooleanExtra("RESULT", false);
                    break;
            }
        }else {
            switch (requestCode){
                case REQUEST_CODE_NEW_WORD:
                    addWordResult = false;
                    break;
            }
        }
    }

}
