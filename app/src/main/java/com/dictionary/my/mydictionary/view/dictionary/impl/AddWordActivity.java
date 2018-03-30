package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
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
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterAddWord;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterAddWordImpl;
import com.dictionary.my.mydictionary.view.dictionary.ViewAddWord;
import com.dictionary.my.mydictionary.view.dictionary.adapters.SpinnerGroupAdapter;
import com.dictionary.my.mydictionary.view.dictionary.adapters.TranslationAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luxso on 18.03.2018.
 */

public class AddWordActivity extends AppCompatActivity implements ViewAddWord {
    private final static String LOG_TAG = "Log_AddWordActivity";
    private PresenterAddWord presenter;
    private EditText wordEditText;
    private EditText alternativeTranslation;
    private Spinner spinner;
    private Translation selectedTranslation;
    private boolean alternativeTranslationMode = false;
    private final static String KEY_ALT_TRANS_MODE = "altTranslationMode";
    private final static String KEY_CURRENT_GROUP = "currentGroup";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()  " + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddWord);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_regular);
        TextView tvTitle = findViewById(R.id.toolbarAddWordTitle);
        tvTitle.setTypeface(typeface);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        typeface = ResourcesCompat.getFont(this, R.font.roboto_light);
        wordEditText = findViewById(R.id.etNewWord);
        wordEditText.setTypeface(typeface);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        wordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(!alternativeTranslationMode) {
                        if(isNetworkAvailable()) {
                            presenter.wordHasPrinted();
                        }else {
                            showERROR("Internet connection is not available");
                        }
                    }
                }
                return false;
            }
        });
        spinner = findViewById(R.id.spinnerAddWord);
        ((TextView)findViewById(R.id.tvForSpinner)).setTypeface(typeface);
        ((TextView)findViewById(R.id.tvSelectedTranslation)).setTypeface(typeface);
        ((TextView)findViewById(R.id.tvOtherTranslate)).setTypeface(typeface);
        if(savedInstanceState != null){
            presenter = (PresenterAddWord) getLastCustomNonConfigurationInstance();
            presenter.attach(this);
            presenter.update();
            alternativeTranslationMode = savedInstanceState.getBoolean(KEY_ALT_TRANS_MODE);
        }else{
            presenter = new PresenterAddWordImpl(getApplicationContext());
            presenter.attach(this);
            presenter.initGroupList();
        }
        initAlternativeTranslationObjects();
    }

    private void initAlternativeTranslationObjects(){
        Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_light);
        alternativeTranslation = findViewById(R.id.etAlternativeTranslation);
        alternativeTranslation.setTypeface(typeface);
        Button btn = findViewById(R.id.btnAddAlternativeTranslation);
        typeface = ResourcesCompat.getFont(this, R.font.roboto_regular);
        btn.setTypeface(typeface);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.translationIsReadyWithoutInternet();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_ALT_TRANS_MODE, alternativeTranslationMode);
        super.onSaveInstanceState(outState);
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
                alternativeTranslationMode = true;
                presenter.alternativeTranslationModeHasSelected();
                break;
            case R.id.add_word_menu_internet:
                alternativeTranslationMode = false;
                presenter.defaultTranslationModeHasSelected();
                break;
        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        findViewById(R.id.pbAddWord).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.pbAddWord).setVisibility(View.GONE);
    }

    @Override
    public void hideAlternativeTranslationMode() {
        findViewById(R.id.etAlternativeTranslation).setVisibility(View.GONE);
        findViewById(R.id.btnAddAlternativeTranslation).setVisibility(View.GONE);
    }

    @Override
    public void showAlternativeTranslationMode() {
        findViewById(R.id.etAlternativeTranslation).setVisibility(View.VISIBLE);
        findViewById(R.id.btnAddAlternativeTranslation).setVisibility(View.VISIBLE);
        alternativeTranslationMode = true;
        invalidateOptionsMenu();
    }

    @Override
    public void hideDefaultTranslationMode() {
        findViewById(R.id.rlSelectedTranslation).setVisibility(View.GONE);
        findViewById(R.id.tvOtherTranslate).setVisibility(View.GONE);
        findViewById(R.id.lvAddWord).setVisibility(View.GONE);

    }

    @Override
    public void showDefaultTranslationMode() {
        findViewById(R.id.rlSelectedTranslation).setVisibility(View.VISIBLE);
        findViewById(R.id.tvOtherTranslate).setVisibility(View.VISIBLE);
        findViewById(R.id.lvAddWord).setVisibility(View.VISIBLE);
        alternativeTranslationMode = false;
        invalidateOptionsMenu();
    }
    @Override
    public void showERROR(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setGroups(ArrayList<Group> groups) {
        Intent intent = getIntent();
        String currentTitle = intent.getStringExtra(KEY_CURRENT_GROUP);
        Integer currentTitlePosition = 0;
        String[] stringGroups = new String[groups.size()];
        if(currentTitle == null) {
            // if activity is opened from allWords fragment and we don't have current title of group
            for (int i = 0; i < groups.size(); i++) {
                stringGroups[i] = groups.get(i).getTitle();
            }
        }else {
            // if activity is opened from groupOfWords activity and we have current title of group
            for (int i = 0; i < groups.size(); i++) {
                stringGroups[i] = groups.get(i).getTitle();
                if(currentTitle.equals(groups.get(i).getTitle())){
                    currentTitlePosition = i;
                }
            }
        }
        SpinnerGroupAdapter groupAdapter = new SpinnerGroupAdapter(this, android.R.layout.simple_spinner_item, stringGroups, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(groupAdapter);
        spinner.setSelection(currentTitlePosition);
    }

    @Override
    public void createListOfTranslation(ArrayList<Translation> words) {
        for (int i = 0; i < words.size(); i++){
            words.get(i).setEng(getPrintedWord());
        }
        final TranslationAdapter adapter = new TranslationAdapter(getApplicationContext(), words);
        ListView listView = findViewById(R.id.lvAddWord);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTranslation = (Translation) adapter.getItem(i);
                ((TextView) findViewById(R.id.tvSelectedTranslation)).setText(selectedTranslation.getRus());
                ImageView imageView = findViewById(R.id.ivSelectedTranslation);
                Picasso.with(getApplicationContext())
                        .load(selectedTranslation.getPreview_image())
                        .into(imageView);
            }
        });

        selectedTranslation = words.get(0);
        ((TextView) findViewById(R.id.tvSelectedTranslation)).setText(selectedTranslation.getRus());
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

    // call this method from presenter, when translation set to db success
    @Override
    public void closeActivity() {
        presenter.destroy();
        Intent intent = new Intent();
        intent.putExtra("RESULT", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.destroy();
    }

    @Override
    public String getPrintedWord() {
        return wordEditText.getText().toString().trim().replaceAll("\\s{2,}", " ").toLowerCase();
    }

    @Override
    public Translation getNewTranslation() {
        Translation translation = selectedTranslation;
        translation.setGroupId(spinner.getSelectedItemId());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd'_'HH:mm");
        String formattedDate = df.format(c);

        translation.setDate(formattedDate);
        Log.d(LOG_TAG, formattedDate);
        return translation;
    }

    @Override
    public Translation getNewTranslationWithoutInternet() {
        Translation translation = new Translation();
        translation.setEng(getPrintedWord());
        translation.setRus(alternativeTranslation.getText().toString().trim().replaceAll("\\s{2,}", " ").toLowerCase());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd'_'HH:mm");
        String formattedDate = df.format(c);

        translation.setDate(formattedDate);
        Log.d(LOG_TAG, formattedDate);
        translation.setGroupId(spinner.getSelectedItemId());
        return translation;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy() " + this.hashCode());
        presenter.detach();
    }
}
