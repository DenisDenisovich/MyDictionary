package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAddWordActivity;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterAddWordActivityImpl;
import com.dictionary.my.mydictionary.view.dictionary.AddWordActivity;
import com.dictionary.my.mydictionary.view.dictionary.adapters.TranslationAdapter;

import java.util.ArrayList;

/**
 * Created by luxso on 18.03.2018.
 */

public class AddWordActivityImpl extends AppCompatActivity implements AddWordActivity{
    private final static String LOG_TAG = "Log_AddWordActivity";
    private PresenterAddWordActivity presenter;
    private EditText wordEditText;
    private Word word = new Word();
    private boolean alternativeTranslationMode = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddWord);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Add new word");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        wordEditText = findViewById(R.id.etNewWord);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        wordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    presenter.wordHasPrinted();
                }
                return false;
            }
        });
        if(savedInstanceState != null){
            presenter = (PresenterAddWordActivity) getLastCustomNonConfigurationInstance();
            presenter.attach(this);
            presenter.update();
        }else{
            presenter = new PresenterAddWordActivityImpl(getApplicationContext());
            presenter.attach(this);
        }

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_word_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(alternativeTranslationMode){
            menu.findItem(R.id.add_word_menu_edit).setVisible(false);
            menu.findItem(R.id.add_word_menu_internet).setVisible(true);
        }else{
            menu.findItem(R.id.add_word_menu_edit).setVisible(true);
            menu.findItem(R.id.add_word_menu_internet).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add_word_menu_edit:
                presenter.alternativeTranslationModeHasSelected();
                break;
            case R.id.add_word_menu_internet:
                presenter.defaultTranslationModeHasSelected();
                break;
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

    }

    @Override
    public void createListOfTranslation(ArrayList<Translation> words) {
        final TranslationAdapter adapter = new TranslationAdapter(this,words);
        ListView listView = findViewById(R.id.lvAddWord);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)findViewById(R.id.tvSelectedTranslation)).setText(((Translation)adapter.getItem(i)).getRus());
            }
        });
    }

    @Override
    public void setAlternativeTranslationMode() {
        alternativeTranslationMode = true;
        invalidateOptionsMenu();
    }

    @Override
    public void setDefaultTranslationMode() {
        alternativeTranslationMode = false;
        invalidateOptionsMenu();
    }

    @Override
    public String getNewWord() {
        return wordEditText.getText().toString().trim().replaceAll("\\s{2,}", " ").toLowerCase();
    }

    @Override
    public Translation getNewTranslation() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
