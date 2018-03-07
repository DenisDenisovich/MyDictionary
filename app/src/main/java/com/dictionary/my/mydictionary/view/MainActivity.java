package com.dictionary.my.mydictionary.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.view.dictionary.impl.ViewAllGroupsImpl;
import com.dictionary.my.mydictionary.view.dictionary.impl.ViewAllWordsImpl;

public class MainActivity extends AppCompatActivity implements ViewAllWordsImpl.onAllWordsSelectedListener, ViewAllGroupsImpl.onAllGroupsSelectedListener {
    private final static String LOG_TAG = "Log_ActivityMain: ";
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
                    ft.replace(R.id.mainActivityContainer,new ViewAllWordsImpl(),KEY_ALL_WORDS);
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
            ft.replace(R.id.mainActivityContainer,new ViewAllWordsImpl(),KEY_ALL_WORDS);
            ft.commit();
        }
    }


    @Override
    public void allWordsScreenSelected() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivityContainer,new ViewAllWordsImpl(),KEY_ALL_WORDS);
        ft.commit();
    }

    @Override
    public void groupOfWordsSelected(long groupId) {

    }

    @Override
    public void allGroupsScreenSelected() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivityContainer,new ViewAllGroupsImpl(),KEY_ALL_GROUPS);
        ft.commit();
    }
}
