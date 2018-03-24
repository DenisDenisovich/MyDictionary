package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;

/**
 * Created by luxso on 08.03.2018.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    private final static String LOG_TAG = "Log_WordAdapter";
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton checkBox;
        public TextView tvWordEng;
        public TextView tvWordRus;
        public ImageButton buttonSound;
        public ViewHolder(View itemView) {
            super(itemView);
            //Log.d(LOG_TAG, "ViewHolder()");
            checkBox = (ImageButton) itemView.findViewById(R.id.check_box);
            tvWordEng = itemView.findViewById(R.id.tvWordEng);
            tvWordRus = itemView.findViewById(R.id.tvWordRus);
            buttonSound = (ImageButton) itemView.findViewById(R.id.btnSound);
        }
    }

    private ArrayList<Word> mdata;
    private ArrayList<Long> selectedItems;
    private PublishSubject<Integer> selectObservable;
    private boolean selectMode = false;
    private Context context;
    public WordAdapter(Context context, ArrayList<Word> data){
        mdata = data;
        selectedItems = new ArrayList<>();
        this.context = context;
        selectObservable = PublishSubject.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(LOG_TAG, "onCreateViewHolder(), selectMod = " + selectMode);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Log.d(LOG_TAG, "onBindViewHolder(), selectMod = " + selectMode + ", position: " + position);
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
            if(selectedItems.contains(mdata.get(position).getId())){

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
        holder.tvWordEng.setText(mdata.get(position).getWord());
        holder.tvWordRus.setText(mdata.get(position).getTranslate());
        holder.buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // тут сделать вызыв воспроизведения слова
            }
        });
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
        if(selectedItems.contains(id)){
            selectedItems.remove(id);
        } else{
            selectedItems.add(id);
        }
        selectObservable.onNext(selectedItems.size());
        notifyDataSetChanged();
    }
    public int getSelectedItemsSize(){
        return selectedItems.size();
    }

    public PublishSubject<Integer> getSelectedItemsObservable(){
        return selectObservable;
    }
    
    public ArrayList<Long> getSelectedItems(){
        return selectedItems;
    }

    public void selectModeOff(){
        selectMode = false;
        selectedItems.clear();
        notifyDataSetChanged();
    }
}
