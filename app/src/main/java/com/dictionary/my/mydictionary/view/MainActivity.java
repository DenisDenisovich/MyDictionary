package com.dictionary.my.mydictionary.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.view.dictionary.AllDictionariesView;
import com.dictionary.my.mydictionary.view.dictionary.DictionaryView;
import com.dictionary.my.mydictionary.view.dictionary.HostToAllDictionariesCommands;
import com.dictionary.my.mydictionary.view.dictionary.HostToDictionaryCommand;
import com.dictionary.my.mydictionary.view.training.EngRusTrainingView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DictionaryListener {
    AllDictionariesView allDictionaries;
    DictionaryView dictionaryView;
    HostToAllDictionariesCommands hostToAllDictionariesCommands;
    HostToDictionaryCommand hostToDictionaryCommands;
    final String KEY_DICTIONARY_ID = "DictionaryId";
    long dictionaryId;
    final String KEY_DICTIONARY_TITLE = "DictionaryTitle";
    String dictionaryTitle;
    Integer itemCount = null;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    final String KEY_FRAGMENT = "Fragment";
    final String KEY_FRAGMENT_ALL_DICTIONARIES = "AllDictionaries";
    final String KEY_FRAGMENT_DICTIONARY = "Dictionary";
    final String KEY_FRAGMENT_TRAINING = "Training";
    final String KEY_DEFAULT_ACTIVITY = "Activity";
    String currentFragment;

    final String KEY_TOOLBAR_MOD = "ToolbarMod";
    final int DEFAULT_TOOLBAR_MOD = 1;
    final int ALL_DICTIONARIES_TOOLBAR_MOD = 2;
    final int DICTIONARY_TOOLBAR_MOD = 3;
    int menuToolbarMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG_TAG", "MainActivity: onCreate()");
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            Log.d("LOG_TAG", "MainActivity: savedInstanceState != null");
            menuToolbarMod = savedInstanceState.getInt(KEY_TOOLBAR_MOD);
            currentFragment = savedInstanceState.getString(KEY_FRAGMENT);
            dictionaryId = savedInstanceState.getLong(KEY_DICTIONARY_ID);
            dictionaryTitle = savedInstanceState.getString(KEY_DICTIONARY_TITLE);
            switch (currentFragment){
                case KEY_FRAGMENT_ALL_DICTIONARIES:
                    hostToAllDictionariesCommands = (AllDictionariesView) getSupportFragmentManager().findFragmentById(R.id.container);
                    itemCount = hostToAllDictionariesCommands.getSizeOfDeleteList();
                    break;
                case KEY_FRAGMENT_DICTIONARY:
                    hostToDictionaryCommands = (DictionaryView) getSupportFragmentManager().findFragmentById(R.id.container);
                    itemCount = hostToDictionaryCommands.getSizeOfDeleteList();
                    break;
                case KEY_FRAGMENT_TRAINING:
                    break;
            }
            invalidateOptionsMenu();
        }else{
            Log.d("LOG_TAG", "MainActivity: savedInstanceState == null");

            allDictionaries = new AllDictionariesView();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,allDictionaries,KEY_FRAGMENT_ALL_DICTIONARIES);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            currentFragment = KEY_FRAGMENT_ALL_DICTIONARIES;
            hostToAllDictionariesCommands = allDictionaries;
            menuToolbarMod = DEFAULT_TOOLBAR_MOD;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // обработка фрагментов пришедших из бекстека
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("LOG_TAG", "MainActivity: addOnBackStackChangedListener()");
                if(getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_ALL_DICTIONARIES) != null && getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_ALL_DICTIONARIES).isVisible()){
                    currentFragment = KEY_FRAGMENT_ALL_DICTIONARIES;
                    hostToAllDictionariesCommands = (AllDictionariesView)getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_ALL_DICTIONARIES);
                }else if(getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_DICTIONARY) != null && getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_DICTIONARY).isVisible()){
                    currentFragment = KEY_FRAGMENT_DICTIONARY;
                    hostToDictionaryCommands = (DictionaryView)getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_DICTIONARY);
                }
                menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("LOG_TAG", "MainActivity: onSaveInstanceState()");
        outState.putInt(KEY_TOOLBAR_MOD, menuToolbarMod);
        outState.putString(KEY_FRAGMENT,currentFragment);
        outState.putLong(KEY_DICTIONARY_ID,dictionaryId);
        outState.putString(KEY_DICTIONARY_TITLE,dictionaryTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.d("LOG_TAG", "MainActivity: onBackPressed()");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(itemCount != null && itemCount != 0){
            switch(menuToolbarMod){
                case ALL_DICTIONARIES_TOOLBAR_MOD:
                    // обработка выхода из режима редактирования (AllDictionariesView)
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    hostToAllDictionariesCommands.resetCheckList();
                    invalidateOptionsMenu();
                    break;
                case DICTIONARY_TOOLBAR_MOD:
                    // обработка выхода из режима редактирования (DictionaryView)
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    hostToDictionaryCommands.resetCheckList();
                    invalidateOptionsMenu();
                    break;
            }
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("LOG_TAG", "MainActivity: onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("LOG_TAG", "MainActivity: onPrepareOptionsMenu()");
        // перерисовка меню в toolbar
        if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
            // если выбран режим редактирования в AllDictionariesView
            if(itemCount == 1){
                menu.findItem(R.id.all_dict_edit_action).setEnabled(true);
            }else{
                menu.findItem(R.id.all_dict_edit_action).setEnabled(false);
            }
            toolbar.setTitle(itemCount.toString());
            menu.setGroupVisible(R.id.default_group,false);
            menu.setGroupVisible(R.id.all_dictionaries_group,true);
            menu.setGroupVisible(R.id.dictionary_group,false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }else if(menuToolbarMod == DICTIONARY_TOOLBAR_MOD){
            // если выбран режим редактирования в DictionaryView
            if(itemCount == 1){
                menu.findItem(R.id.dict_edit_action).setEnabled(true);
            }else{
                menu.findItem(R.id.dict_edit_action).setEnabled(false);
            }
            toolbar.setTitle(itemCount.toString());
            menu.setGroupVisible(R.id.default_group,false);
            menu.setGroupVisible(R.id.all_dictionaries_group,false);
            menu.setGroupVisible(R.id.dictionary_group,true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        }else if(menuToolbarMod == DEFAULT_TOOLBAR_MOD){
            // если выбран default режим
            menu.setGroupVisible(R.id.all_dictionaries_group,false);
            menu.setGroupVisible(R.id.dictionary_group,false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
            itemCount = 0;
            switch (currentFragment){
                case KEY_FRAGMENT_ALL_DICTIONARIES:
                    menu.setGroupVisible(R.id.default_group,false);
                    toolbar.setTitle(getResources().getString(R.string.title_all_dictionaries));
                    break;
                case KEY_FRAGMENT_DICTIONARY:
                    menu.setGroupVisible(R.id.default_group,true);
                    toolbar.setTitle(dictionaryTitle);
                    break;
                case KEY_FRAGMENT_TRAINING:
                    menu.setGroupVisible(R.id.default_group,false);
                    toolbar.setTitle(getResources().getString(R.string.title_all_dictionaries));
                    break;
                case KEY_DEFAULT_ACTIVITY:
                    menu.setGroupVisible(R.id.default_group,false);
                    toolbar.setTitle(getResources().getString(R.string.title_activity_main));
                    break;

            }

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("LOG_TAG", "MainActivity: onOptionsItemSelected()");
        if (id == android.R.id.home) {
            switch(menuToolbarMod){
                case ALL_DICTIONARIES_TOOLBAR_MOD:
                    // обработка выхода из режима редактирования (AllDictionariesView)
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    hostToAllDictionariesCommands.resetCheckList();
                    invalidateOptionsMenu();
                    return true;
                case DICTIONARY_TOOLBAR_MOD:
                    // обработка выхода из режима редактирования (DictionaryView)
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    hostToDictionaryCommands.resetCheckList();
                    invalidateOptionsMenu();
                    return true;
                case DEFAULT_TOOLBAR_MOD:
                    // обработка стандартного режима (открытие NavigationView)
                    toggle.onOptionsItemSelected(item);
                    return true;
            }
        }
        // обработка пунктов меню в режиме редактирования AllDictionariesView
        if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
            switch (id){
                case R.id.all_dict_delete_action:
                    hostToAllDictionariesCommands.deleteSelectedDictionaries();
                    return true;
                case R.id.all_dict_edit_action:
                    hostToAllDictionariesCommands.editSelectedDictionary();
                    return true;
            }
        }
        // обработка пунктов меню в режиме редактирования DictionaryView
        if(menuToolbarMod == DICTIONARY_TOOLBAR_MOD){
            switch (id){
                case R.id.dict_delete_action:
                    hostToDictionaryCommands.deleteSelectedWords();
                    return true;
                case R.id.dict_move_action:
                    hostToDictionaryCommands.moveSelectedWords();
                    return true;
                case R.id.dict_edit_action:
                    hostToDictionaryCommands.editSelectedDictionary();
                    return true;
            }
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("LOG_TAG", "MainActivity: onNavigationItemSelected()");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++){
            getSupportFragmentManager().popBackStack();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_dictionary) {
            allDictionaries = new AllDictionariesView();
            fragmentTransaction.replace(R.id.container,allDictionaries,KEY_FRAGMENT_ALL_DICTIONARIES);
            fragmentTransaction.addToBackStack(null);
            currentFragment = KEY_FRAGMENT_ALL_DICTIONARIES;
            hostToAllDictionariesCommands = allDictionaries;
        }  else if (id == R.id.nav_training) {
            fragmentTransaction.replace(R.id.container,new EngRusTrainingView(),KEY_FRAGMENT_TRAINING);
            currentFragment = KEY_FRAGMENT_TRAINING;
        } else if (id == R.id.nav_setting) {

        }
        fragmentTransaction.commit();
        menuToolbarMod = DEFAULT_TOOLBAR_MOD;
        invalidateOptionsMenu();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void selectedDictionary(long dictionaryId, String dictionaryTitle) {
        Log.d("LOG_TAG", "MainActivity: selectDictionary()");
        this.dictionaryId = dictionaryId;
        this.dictionaryTitle = dictionaryTitle;
        dictionaryView = new DictionaryView();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,dictionaryView,KEY_FRAGMENT_DICTIONARY);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public long getDictionary() {
        Log.d("LOG_TAG", "MainActivity: getDictionary()");
        return dictionaryId;
    }

    @Override
    public void checkListIsChange() {
        Log.d("LOG_TAG", "MainActivity: checkListIsChange()");
        switch (currentFragment){
            case KEY_FRAGMENT_ALL_DICTIONARIES:
                itemCount = hostToAllDictionariesCommands.getSizeOfDeleteList();
                if (itemCount == 0) {
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                } else {
                    menuToolbarMod = ALL_DICTIONARIES_TOOLBAR_MOD;
                }
                break;
            case KEY_FRAGMENT_DICTIONARY:
                itemCount = hostToDictionaryCommands.getSizeOfDeleteList();
                if (itemCount == 0) {
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                } else {
                    menuToolbarMod = DICTIONARY_TOOLBAR_MOD;
                }
                break;
        }
        invalidateOptionsMenu();
    }

}
