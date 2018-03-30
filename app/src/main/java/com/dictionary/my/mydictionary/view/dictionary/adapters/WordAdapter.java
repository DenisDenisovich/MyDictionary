package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by luxso on 08.03.2018.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> implements MediaPlayer.OnPreparedListener,
                                                                                         MediaPlayer.OnCompletionListener,
                                                                                         MediaPlayer.OnErrorListener{
    private final static String LOG_TAG = "Log_WordAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton checkBox;
        public TextView tvWordEng;
        public TextView tvWordRus;
        public ImageButton buttonSound;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (ImageButton) itemView.findViewById(R.id.check_box);
            tvWordEng = itemView.findViewById(R.id.tvWordEng);
            tvWordRus = itemView.findViewById(R.id.tvWordRus);
            buttonSound = (ImageButton) itemView.findViewById(R.id.btnSound);
        }
    }

    private ArrayList<Word> mdata;
    private ArrayList<Long> selectedItemIds;
    private PublishSubject<Integer> selectObservable;
    MediaPlayer mediaPlayer;
    private boolean selectMode = false;
    private Context context;
    private boolean soundIsWorking = false;
    public WordAdapter(Context context, ArrayList<Word> data){
        mdata = data;
        selectedItemIds = new ArrayList<>();
        this.context = context;
        selectObservable = PublishSubject.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if(selectMode){
            int marginTop = (int)context.getResources().getDimension(R.dimen.llWord_top_margin);
            int marginBottom = (int)context.getResources().getDimension(R.dimen.llWord_bottom_margin);
            int marginStart = (int)context.getResources().getDimension(R.dimen.llWord_start_margin_selected);
            lp.setMargins(marginStart,marginTop,0,marginBottom);
            holder.itemView.findViewById(R.id.llWords).setLayoutParams(lp);

            holder.checkBox.setVisibility(View.VISIBLE);
            if(selectedItemIds.contains(mdata.get(position).getId())){

                holder.checkBox.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_check_box_selected));
            }else{
                holder.checkBox.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_check_box));
            }

        }else{
            holder.checkBox.setVisibility(View.GONE);
            int marginTop = (int)context.getResources().getDimension(R.dimen.llWord_top_margin);
            int marginBottom = (int)context.getResources().getDimension(R.dimen.llWord_bottom_margin);
            int marginStart = (int)context.getResources().getDimension(R.dimen.llWord_start_margin);
            lp.setMargins(marginStart,marginTop,0,marginBottom);
            holder.itemView.findViewById(R.id.llWords).setLayoutParams(lp);
        }
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light);
        holder.tvWordEng.setText(mdata.get(position).getWord());
        holder.tvWordRus.setText(mdata.get(position).getTranslate());
        holder.tvWordEng.setTypeface(typeface);
        holder.tvWordRus.setTypeface(typeface);
        holder.buttonSound.setImageResource(R.drawable.ic_word_item_sound);
        // if current word have sound
        if(mdata.get(position).getSound() != null) {
            holder.buttonSound.setVisibility(View.VISIBLE);
            holder.buttonSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isNetworkAvailable()) {
                        // if sound is't used now
                        if (!soundIsWorking) {
                            holder.buttonSound.setImageResource(R.drawable.ic_word_item_sound_activity);
                            playSoundOfWord(mdata.get(position).getSound());
                        }
                    }else {
                        Toast.makeText(context, "Internet connection is not available", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            holder.buttonSound.setVisibility(View.GONE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectMode = true;
                selectItem(mdata.get(position).getId());
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectMode) {
                    selectItem(mdata.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    private void selectItem(Long id){
        if(selectedItemIds.contains(id)){
            selectedItemIds.remove(id);
        } else{
            selectedItemIds.add(id);
        }
        selectObservable.onNext(selectedItemIds.size());
        notifyDataSetChanged();
    }

    public int getSelectedItemsSize(){
        return selectedItemIds.size();
    }

    public PublishSubject<Integer> getSelectedItemsObservable(){
        return selectObservable;
    }
    
    public ArrayList<Long> getSelectedItemIds(){
        return selectedItemIds;
    }

    public Boolean getSelectMode(){
        return selectMode;
    }

    public void setSelectedItemIds(ArrayList<Long> selectedItemIds){
        this.selectedItemIds = selectedItemIds;
    }

    public void setSelectMode(Boolean selectMode){
        this.selectMode = selectMode;
    }

    public void selectModeOff(){
        selectMode = false;
        selectedItemIds.clear();
        notifyDataSetChanged();
    }

    public void deleteSelectedWords(){
        ArrayList<Word> deleteList = new ArrayList<>();
        int dataSize = mdata.size();
        for(int i = 0; i < mdata.size(); i++){
            if(selectedItemIds.contains(mdata.get(i).getId())){
                deleteList.add(mdata.get(i));
            }
        }
        mdata.removeAll(deleteList);
        selectModeOff();

    }

    private void playSoundOfWord(String s){
        try {
            soundIsWorking = true;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(s);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(LOG_TAG, "onPrepared()");
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(LOG_TAG, "onCompletion()");
        mediaPlayer.release();
        soundIsWorking = false;
        notifyDataSetChanged();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(LOG_TAG, "onError()");
        soundIsWorking = false;
        notifyDataSetChanged();
        return true;
    }

    public void destroy(){
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
