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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAddWordActivity;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterAddWordActivityImpl;
import com.dictionary.my.mydictionary.view.dictionary.AddWordActivity;
import com.dictionary.my.mydictionary.view.dictionary.adapters.GroupAdapter;
import com.dictionary.my.mydictionary.view.dictionary.adapters.TranslationAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luxso on 18.03.2018.
 */

public class AddWordActivityImpl extends AppCompatActivity implements AddWordActivity{
    private final static String LOG_TAG = "Log_AddWordActivity";
    private PresenterAddWordActivity presenter;
    private EditText wordEditText;
    private EditText alternativeTranslation;
    private Spinner spinner;
    private GroupAdapter groupAdapter;
    private Translation selectedTranslation;
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
        spinner = findViewById(R.id.spinnerAddWord);

        if(savedInstanceState != null){
            presenter = (PresenterAddWordActivity) getLastCustomNonConfigurationInstance();
            presenter.attach(this);
            presenter.update();
        }else{
            presenter = new PresenterAddWordActivityImpl(getApplicationContext());
            presenter.attach(this);
            presenter.initGroupList();
        }
        initAlternativeTranslationObjects();
    }

    private void initAlternativeTranslationObjects(){
        alternativeTranslation = findViewById(R.id.etAlternativeTranslation);
        Button btn = findViewById(R.id.btnAddAlternativeTranslation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.translationIsReady();
            }
        });
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
        findViewById(R.id.pbAddWord).setVisibility(View.VISIBLE);
        findViewById(R.id.rlSelectedTranslation).setVisibility(View.GONE);
        findViewById(R.id.tvOtherTranslate).setVisibility(View.GONE);
        findViewById(R.id.lvAddWord).setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.pbAddWord).setVisibility(View.GONE);
        findViewById(R.id.rlSelectedTranslation).setVisibility(View.VISIBLE);
        findViewById(R.id.tvOtherTranslate).setVisibility(View.VISIBLE);
        findViewById(R.id.lvAddWord).setVisibility(View.VISIBLE);
    }

    @Override
    public void showERROR(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setGroups(ArrayList<Group> groups) {
        String[] stringGroups = new String[groups.size()];
        for(int i = 0; i < groups.size(); i++){
            stringGroups[i] = groups.get(i).getTitle();
        }
        groupAdapter = new GroupAdapter(this, android.R.layout.simple_spinner_item, stringGroups, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(groupAdapter);
        spinner.setSelection(0);
    }

    @Override
    public void createListOfTranslation(ArrayList<Translation> words) {
        final TranslationAdapter adapter = new TranslationAdapter(getApplicationContext(),words);
        ListView listView = findViewById(R.id.lvAddWord);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTranslation = (Translation)adapter.getItem(i);
                ((TextView)findViewById(R.id.tvSelectedTranslation)).setText(selectedTranslation.getRus());
                ImageView imageView = findViewById(R.id.ivSelectedTranslation);
                Picasso.with(getApplicationContext())
                        .load(selectedTranslation.getPreview_image())
                        .into(imageView);

            }
        });

        selectedTranslation = words.get(0);
        ((TextView)findViewById(R.id.tvSelectedTranslation)).setText(selectedTranslation.getRus());
        ImageView imageView = findViewById(R.id.ivSelectedTranslation);
        ImageButton btn = (ImageButton) findViewById(R.id.btnAddWord);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.translationIsReady();
            }
        });
        Picasso.with(getApplicationContext())
                .load(selectedTranslation.getPreview_image())
                .into(imageView);
    }

    @Override
    public void setAlternativeTranslationMode() {
        alternativeTranslationMode = true;
        findViewById(R.id.rlSelectedTranslation).setVisibility(View.GONE);
        findViewById(R.id.tvOtherTranslate).setVisibility(View.GONE);
        findViewById(R.id.lvAddWord).setVisibility(View.GONE);
        findViewById(R.id.etAlternativeTranslation).setVisibility(View.VISIBLE);
        findViewById(R.id.btnAddAlternativeTranslation).setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    @Override
    public void setDefaultTranslationMode() {
        alternativeTranslationMode = false;
        findViewById(R.id.rlSelectedTranslation).setVisibility(View.GONE);
        findViewById(R.id.tvOtherTranslate).setVisibility(View.GONE);
        findViewById(R.id.lvAddWord).setVisibility(View.GONE);
        findViewById(R.id.etAlternativeTranslation).setVisibility(View.GONE);
        findViewById(R.id.btnAddAlternativeTranslation).setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public String getPrintedWord() {
        return wordEditText.getText().toString().trim().replaceAll("\\s{2,}", " ").toLowerCase();
    }

    @Override
    public Translation getNewTranslation() {
        Translation translation;
        if(selectedTranslation != null) {
            translation = selectedTranslation;
        }else{
            translation = new Translation();
            translation.setRus(alternativeTranslation.getText().toString());
        }
        translation.setEng(wordEditText.getText().toString());
        translation.setGroupId(spinner.getSelectedItemId());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd'_'HH:mm");
        String formattedDate = df.format(c);
        translation.setDate(formattedDate);
        Log.d(LOG_TAG, formattedDate);
        return translation;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
