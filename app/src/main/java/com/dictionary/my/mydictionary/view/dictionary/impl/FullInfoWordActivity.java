package com.dictionary.my.mydictionary.view.dictionary.impl;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.CBKeys;
import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.Example;
import com.dictionary.my.mydictionary.domain.entites.SoundPlayer;
import com.dictionary.my.mydictionary.domain.entites.dictionary.WordFullInformation;
import com.dictionary.my.mydictionary.presenter.dictionary.PresenterFullInfoWord;
import com.dictionary.my.mydictionary.presenter.dictionary.impl.PresenterFullInfoWordImpl;
import com.dictionary.my.mydictionary.view.dictionary.ViewFullInfoWord;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by luxso on 30.05.2018.
 */

public class FullInfoWordActivity extends AppCompatActivity implements ViewFullInfoWord{
    private final static String LOG_TAG = "Log_FIWActivity";
    PresenterFullInfoWord presenter;
    private boolean soundIsWorking;
    private SoundPlayer soundPlayer;
    private DisposableObserver soundDisposable;
    private final static String KEY_SOUND_STATE = "soundState";
    private Map<String, String> partOfSpeechCodes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            String id = intent.getStringExtra(CBKeys.KEY_ID);
            presenter = new PresenterFullInfoWordImpl(this);
            presenter.attach(this);
            presenter.init(id);
            soundIsWorking = false;
        }else{
            presenter = (PresenterFullInfoWord) getLastCustomNonConfigurationInstance();
            presenter.attach(this);
            presenter.update();
            soundIsWorking = savedInstanceState.getBoolean(KEY_SOUND_STATE);
        }
        partOfSpeechCodes = new HashMap<>();
        partOfSpeechCodes.put("n","noun");
        partOfSpeechCodes.put("v","verb");
        partOfSpeechCodes.put("j","adjective");
        partOfSpeechCodes.put("r", "adverb");
        partOfSpeechCodes.put("prp", "preposition");
        partOfSpeechCodes.put("prn", "pronoun");
        partOfSpeechCodes.put("crd", "cardinal number");
        partOfSpeechCodes.put("cjc", "conjunction");
        partOfSpeechCodes.put("exc",  "interjection");
        partOfSpeechCodes.put("det", "article");
        partOfSpeechCodes.put("abb", "abbreviation");
        partOfSpeechCodes.put("x", "particle");
        partOfSpeechCodes.put("ord", "ordinal number");
        partOfSpeechCodes.put("md", "modal verb");
        partOfSpeechCodes.put("ph", "phrase");
        partOfSpeechCodes.put("phi", "idiom");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_SOUND_STATE,soundIsWorking);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showERROR(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWord(WordFullInformation word) {
        Log.d(LOG_TAG, "setWord()");
        ImageView imageView = findViewById(R.id.fullInfoImage);
        if(word.getImage() != null) {
            Picasso.with(this)
                    .load(word.getImage())
                    .resize(1500, 1000)
                    .into(imageView);
        }else{
            imageView.setVisibility(View.GONE);
        }
        ((TextView)findViewById(R.id.fullInfoTVEng)).setText(word.getEng());
        ((TextView)findViewById(R.id.fullInfoTVRus)).setText(word.getRus());
        if(word.getSound() != null) {
            findViewById(R.id.btnSound).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SoundPlayer.isNetworkAvailable(getApplicationContext())) {
                        try {
                            // if sound is't working now
                            if (!soundIsWorking) {
                                soundIsWorking = true;
                                ((ImageButton) findViewById(R.id.btnSound)).setImageResource(R.drawable.ic_word_item_sound_activity);
                                soundPlayer = new SoundPlayer(word.getSound());
                                subscribeToSound(soundPlayer.getStateObservable());
                                soundPlayer.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            soundIsWorking = false;
                            ((ImageButton) findViewById(R.id.btnSound)).setImageResource(R.drawable.ic_word_item_sound);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            findViewById(R.id.btnSound).setVisibility(View.GONE);
        }
        if(word.getDefinition() != null){
            ((TextView)findViewById(R.id.fullInfoTVDefinition)).setText(word.getDefinition());
        }else{
            findViewById(R.id.fullInfoTVDefinitionLabel).setVisibility(View.GONE);
        }
        if(word.getPartOfSpeech() != null){
            ((TextView)findViewById(R.id.fullInfoTVPartOfSpeech)).setText(partOfSpeechCodes.get(word.getPartOfSpeech()));
        }else{
            findViewById(R.id.fullInfoTVPartOfSpeechLabel).setVisibility(View.GONE);
        }
        if(word.getTranscription() != null){
            ((TextView)findViewById(R.id.fullInfoTVTranscription)).setText(word.getTranscription());
        }else{
            findViewById(R.id.fullInfoTVTranscriptionLabel).setVisibility(View.GONE);
        }
        if(word.getExamples() != null){
            TextView tvLabelEx = new TextView(this);
            LinearLayout.LayoutParams lpLabelEx = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int marginTop = (int)getResources().getDimension(R.dimen.llWord_top_margin);
            int marginStart = (int)getResources().getDimension(R.dimen.llWord_start_margin);
            lpLabelEx.setMargins(marginStart,marginTop,0,0);
            tvLabelEx.setLayoutParams(lpLabelEx);
            tvLabelEx.setText("Examples: ");
            tvLabelEx.setTextColor(Color.argb(255,89,89,89));
            tvLabelEx.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_light);
            tvLabelEx.setTypeface(typeface);
            ((LinearLayout)findViewById(R.id.container)).addView(tvLabelEx);
            LinearLayout.LayoutParams lpExample = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);;
            lpExample.setMargins(marginStart,0,0,0);
            TextView tvExample;
            for(Example e: word.getExamples()){
                tvExample = new TextView(this);
                tvExample.setLayoutParams(lpExample);
                tvExample.setText(e.getText());
                tvExample.setTextColor(Color.BLACK);
                tvExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvExample.setTypeface(typeface);
                ((LinearLayout)findViewById(R.id.container)).addView(tvExample);
            }

        }
    }

    private void subscribeToSound(PublishSubject<Boolean> observable){
        if(soundDisposable != null){
            soundDisposable.dispose();
        }
        soundDisposable = observable.subscribeWith(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                soundIsWorking = false;
                ((ImageButton)findViewById(R.id.btnSound)).setImageResource(R.drawable.ic_word_item_sound);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(soundPlayer != null){
            soundPlayer.destroy();
        }
        if(soundDisposable != null){
            soundDisposable.dispose();
        }
        presenter.destroy();
    }
}
