package com.dictionary.my.mydictionary.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
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
import com.dictionary.my.mydictionary.view.training.EngRusTrainingView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DictionaryListener {
    HostToAllDictionariesCommands allDictListener;
    AllDictionariesView allDictionaries;
    DictionaryView dictionaryView;
    HostToAllDictionariesCommands allDictionariesCommands;
    long selectedDictionary;
    Integer deletedItemCount = null;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    final String KEY_TOOLBAR_MOD = "ToolbarMod";
    final String KEY_FRAGMENT = "Fragment";
    final String KEY_FRAGMENT_ALL_DICTIONARIES = "AllDictionaries";
    final String KEY_FRAGMENT_DICTIONARY = "Dictionary";
    final String KEY_FRAGMENT_TRAINING = "Training";
    String currentFragment;
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
                    Log.d("LOG_TAG", "Yes");
                    allDictionariesCommands = (AllDictionariesView) getSupportFragmentManager().findFragmentById(R.id.container);
                    deletedItemCount = allDictionariesCommands.getDeletedItemCount();
                    break;
                case KEY_FRAGMENT_DICTIONARY:
                    break;
                case KEY_FRAGMENT_TRAINING:
                    break;
            }
            invalidateOptionsMenu();
        }else{
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
            if(deletedItemCount == 1){
                menu.findItem(R.id.all_dict_delete_with_words).setEnabled(true);
                menu.findItem(R.id.all_dict_edit_action).setEnabled(true);
            }else{
                menu.findItem(R.id.all_dict_delete_with_words).setEnabled(false);
                menu.findItem(R.id.all_dict_edit_action).setEnabled(false);
            }
            toolbar.setTitle(deletedItemCount.toString());
            menu.setGroupVisible(R.id.default_group,false);
            menu.setGroupVisible(R.id.all_dictionaries_group,true);
            menu.setGroupVisible(R.id.dictionary_group,false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }else if(menuToolbarMod == DICTIONARY_TOOLBAR_MOD){

        }else if(menuToolbarMod == DEFAULT_TOOLBAR_MOD){
            menu.setGroupVisible(R.id.default_group,true);
            menu.setGroupVisible(R.id.all_dictionaries_group,false);
            menu.setGroupVisible(R.id.dictionary_group,false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
            toolbar.setTitle("All Dictionaries");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
                menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                allDictionariesCommands.cancelDeletedDictionaries();
                invalidateOptionsMenu();
                return true;
            }else if(menuToolbarMod == DICTIONARY_TOOLBAR_MOD){
                return true;
            }else if(menuToolbarMod == DEFAULT_TOOLBAR_MOD) {
                toggle.onOptionsItemSelected(item);
                return true;
            }
        }

        if(menuToolbarMod == ALL_DICTIONARIES_TOOLBAR_MOD){
            switch (item.getItemId()){
                case R.id.all_dict_delete_action:
                    menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    allDictionariesCommands.deleteSelectedDictionaries();
                    invalidateOptionsMenu();
                    break;
                case R.id.all_dict_delete_with_words:
                    break;
                case R.id.all_dict_edit_action:
                    //menuToolbarMod = DEFAULT_TOOLBAR_MOD;
                    allDictionariesCommands.editSelectedDictionary();
                    //invalidateOptionsMenu();
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
            fragmentTransaction.replace(R.id.container,allDictionaries);
            currentFragment = KEY_FRAGMENT_ALL_DICTIONARIES;
            allDictionariesCommands = allDictionaries;
            toolbar.setTitle("All Dictionaries");
        }  else if (id == R.id.nav_training) {
            fragmentTransaction.replace(R.id.container,new EngRusTrainingView());
            currentFragment = KEY_FRAGMENT_TRAINING;
            toolbar.setTitle("Training");
        } else if (id == R.id.nav_setting) {

        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setDictionary(long dictionaryId) {
        selectedDictionary = dictionaryId;
    }

    @Override
    public void openNewDictionary() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new DictionaryView());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public long getDictionary() {
        return selectedDictionary;
    }

    @Override
    public void startChangeItemMod() {
        menuToolbarMod = ALL_DICTIONARIES_TOOLBAR_MOD;
        invalidateOptionsMenu();
    }

    @Override
    public void endChangeItemMod() {
        menuToolbarMod = DEFAULT_TOOLBAR_MOD;
        invalidateOptionsMenu();
    }

    @Override
    public void setDeletedItemCount(int delCount) {
        if(delCount == 0) {
            deletedItemCount = null;
        }else{
            deletedItemCount = delCount;
        }
        invalidateOptionsMenu();
    }
}
