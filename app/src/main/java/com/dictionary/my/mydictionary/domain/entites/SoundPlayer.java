package com.dictionary.my.mydictionary.domain.entites;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import io.reactivex.subjects.PublishSubject;

/**
 * This class is used for playing sound of words. He can emit state of working also
 */

public class SoundPlayer implements MediaPlayer.OnPreparedListener,
                                    MediaPlayer.OnCompletionListener,
                                    MediaPlayer.OnErrorListener {


    private MediaPlayer mediaPlayer;
    private boolean soundIsWorking = false;
    private PublishSubject<Boolean> stateObservable;
    public SoundPlayer(String url) throws IOException{
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        stateObservable = PublishSubject.create();
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else
            return false;
    }

    public void start(){
        mediaPlayer.prepareAsync();
        soundIsWorking = true;
        stateObservable.onNext(soundIsWorking);
    }

    public PublishSubject<Boolean> getStateObservable(){
        return stateObservable;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.release();
        soundIsWorking = false;
        stateObservable.onNext(soundIsWorking);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.release();
        soundIsWorking = false;
        stateObservable.onNext(soundIsWorking);
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public void destroy(){
        mediaPlayer.release();
    }
}
