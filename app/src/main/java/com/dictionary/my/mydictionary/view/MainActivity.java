package com.dictionary.my.mydictionary.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.view.dictionary.impl.AddWordActivityImpl;
import com.dictionary.my.mydictionary.view.dictionary.impl.AllGroupsFragmentImpl;
import com.dictionary.my.mydictionary.view.dictionary.impl.AllWordsFragmentImpl;
import com.dictionary.my.mydictionary.view.dictionary.impl.GroupOfWordsActivityImpl;

public class MainActivity extends AppCompatActivity implements AllWordsFragmentImpl.onAllWordsSelectedListener, AllGroupsFragmentImpl.onAllGroupsSelectedListener {
    private final int REQUEST_CODE_NEW_WORD = 1;
    private final int REQEST_CODE_GROUP_OF_WORDS = 2;

    private final static String LOG_TAG = "Log_ActivityMain";
    private final static String KEY_ALL_WORDS = "allWordsFragment";
    private final static String KEY_ALL_GROUPS = "allGroupsFragment";
    private final static String KEY_TRAININGS = "trainingsFragment";
    private final static String KEY_NOTEPAD = "notepadFragment";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_dictionary:
                    ft.replace(R.id.mainActivityContainer,new AllWordsFragmentImpl(),KEY_ALL_WORDS);
                    ft.commit();
                    return true;
                case R.id.navigation_trainings:
                    return true;
                case R.id.navigation_notepad:
                   // mTextMessage.setText(R.string.title_notifications);
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainActivityContainer,new AllWordsFragmentImpl(),KEY_ALL_WORDS);
            ft.commit();
        }
    }


    @Override
    public void allWordsScreenSelected() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivityContainer,new AllWordsFragmentImpl(),KEY_ALL_WORDS);
        ft.commit();
    }

    @Override
    public void groupOfWordsSelected(long groupId, String title) {
        Intent intent = new Intent(this,GroupOfWordsActivityImpl.class);
        intent.putExtra(Content.COLUMN_ROWID, groupId);
        intent.putExtra(Content.COLUMN_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void allGroupsScreenSelected() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivityContainer,new AllGroupsFragmentImpl(),KEY_ALL_GROUPS);
        ft.commit();
    }

    @Override
    public void showAddWordDialog() {
        Intent intent = new Intent(this,AddWordActivityImpl.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_WORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NEW_WORD:
                    allWordsScreenSelected();
                    break;
            }
        }
    }
}
