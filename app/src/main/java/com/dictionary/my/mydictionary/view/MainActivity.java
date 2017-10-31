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
    long dictionaryId;
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
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            menuToolbarMod = savedInstanceState.getInt(KEY_TOOLBAR_MOD);
            currentFragment = savedInstanceState.getString(KEY_FRAGMENT);
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
            menuToolbarMod = DEFAULT_TOOLBAR_MOD;
            currentFragment = KEY_DEFAULT_ACTIVITY;
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

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().findFragmentByTag(KEY_DEFAULT_ACTIVITY) != null && getSupportFragmentManager().findFragmentByTag(KEY_DEFAULT_ACTIVITY).isVisible()){
                    currentFragment = KEY_DEFAULT_ACTIVITY;
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                }else if(getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_ALL_DICTIONARIES) != null && getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_ALL_DICTIONARIES).isVisible()){
                    currentFragment = KEY_FRAGMENT_ALL_DICTIONARIES;
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                }else if(getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_DICTIONARY) != null && getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_DICTIONARY).isVisible()){
                    currentFragment = KEY_FRAGMENT_DICTIONARY;
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                }else if(getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_TRAINING) != null && getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_TRAINING).isVisible()){
                    currentFragment = KEY_FRAGMENT_TRAINING;
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_TOOLBAR_MOD, menuToolbarMod);
        outState.putString(KEY_FRAGMENT,currentFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
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
            menu.setGroupVisible(R.id.default_group,true);
            menu.setGroupVisible(R.id.all_dictionaries_group,false);
            menu.setGroupVisible(R.id.dictionary_group,false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

            switch (currentFragment){
                case KEY_FRAGMENT_ALL_DICTIONARIES:
                    toolbar.setTitle(getResources().getString(R.string.title_all_dictionaries));
                    break;
                case KEY_FRAGMENT_DICTIONARY:
                    toolbar.setTitle(dictionaryTitle);
                    break;
                case KEY_FRAGMENT_TRAINING:
                    toolbar.setTitle(getResources().getString(R.string.title_all_dictionaries));
                    break;
                case KEY_DEFAULT_ACTIVITY:
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

        if (id == android.R.id.home) {
            if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
                menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                hostToAllDictionariesCommands.resetCheckList();
                invalidateOptionsMenu();
                return true;
            }else if(menuToolbarMod == DICTIONARY_TOOLBAR_MOD){
                menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                hostToDictionaryCommands.resetCheckList();
                invalidateOptionsMenu();
                return true;
            }else if(menuToolbarMod == DEFAULT_TOOLBAR_MOD) {
                toggle.onOptionsItemSelected(item);
                return true;
            }
        }

        if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
            switch (id){
                case R.id.all_dict_delete_action:
                    hostToAllDictionariesCommands.deleteSelectedDictionaries();
                    break;
                case R.id.all_dict_edit_action:
                    hostToAllDictionariesCommands.editSelectedDictionary();
                    break;
            }
        }

        if(menuToolbarMod == DICTIONARY_TOOLBAR_MOD){
            switch (id){
                case R.id.dict_delete_action:
                    hostToDictionaryCommands.deleteSelectedWords();
                    break;
                case R.id.dict_move_action:
                    hostToDictionaryCommands.moveSelectedWords();
                    break;
                case R.id.dict_edit_action:
                    hostToDictionaryCommands.editSelectedDictionary();
                    break;
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_dictionary) {
            allDictionaries = new AllDictionariesView();
            fragmentTransaction.replace(R.id.container,allDictionaries,KEY_FRAGMENT_ALL_DICTIONARIES);
            currentFragment = KEY_FRAGMENT_ALL_DICTIONARIES;
            hostToAllDictionariesCommands = allDictionaries;
            toolbar.setTitle(getResources().getString(R.string.title_all_dictionaries));
        }  else if (id == R.id.nav_training) {
            fragmentTransaction.replace(R.id.container,new EngRusTrainingView(),KEY_FRAGMENT_TRAINING);
            currentFragment = KEY_FRAGMENT_TRAINING;
            toolbar.setTitle(getResources().getString(R.string.item_navigation_drawer_training));
        } else if (id == R.id.nav_setting) {

        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void selectedDictionary(long dictionaryId, String dictionaryTitle) {
        this.dictionaryId = dictionaryId;
        this.dictionaryTitle = dictionaryTitle;
        dictionaryView = new DictionaryView();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,dictionaryView,KEY_FRAGMENT_DICTIONARY);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        currentFragment = KEY_FRAGMENT_DICTIONARY;
        hostToDictionaryCommands = dictionaryView;
    }

    @Override
    public long getDictionary() {
        return dictionaryId;
    }

    @Override
    public void checkListIsChange() {
        switch (currentFragment){
            case KEY_FRAGMENT_ALL_DICTIONARIES:
                itemCount = hostToAllDictionariesCommands.getSizeOfDeleteList();
                if (itemCount == 0) {
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                } else {
                    menuToolbarMod = ALL_DICTIONARIES_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                }
                break;
            case KEY_FRAGMENT_DICTIONARY:
                itemCount = hostToDictionaryCommands.getSizeOfDeleteList();
                if (itemCount == 0) {
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                } else {
                    menuToolbarMod = DICTIONARY_TOOLBAR_MOD;
                    invalidateOptionsMenu();
                }
                break;
        }
    }

}
