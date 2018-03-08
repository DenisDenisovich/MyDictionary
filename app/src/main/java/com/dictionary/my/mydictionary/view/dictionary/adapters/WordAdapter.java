package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.data.entites.Word;

import java.util.ArrayList;

/**
 * Created by luxso on 08.03.2018.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvWordEng;
        public TextView tvWordRus;
        public ImageButton buttonSound;
        public ViewHolder(View itemView) {
            super(itemView);
            tvWordEng = itemView.findViewById(R.id.tvWordEng);
            tvWordRus = itemView.findViewById(R.id.tvWordRus);
            buttonSound = (ImageButton)itemView.findViewById(R.id.btnSound);
        }
    }

    private ArrayList<Word> mdata;
    public WordAdapter(ArrayList<Word> data){
        mdata = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvWordEng.setText(mdata.get(position).getWord());
        holder.tvWordRus.setText(mdata.get(position).getTranslate());
        holder.buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
