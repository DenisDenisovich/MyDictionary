package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Translation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by luxso on 22.03.2018.
 */

public class TranslationAdapter extends BaseAdapter {
    private final static String LOG_TAG = "Log_TranslationAdapter";
    private ArrayList<Translation> data;
    private int selectedItem;
    private LayoutInflater layoutInflater;
    private Context context;
    public TranslationAdapter(Context context, ArrayList<Translation> data){
        this.context = context;
        this.data = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_translation, null);
        }

        ImageView imageView = view.findViewById(R.id.ivListTranslation);
        Picasso.with(context)
                .load(data.get(i).getPreview_image())
                .into(imageView);


        ((TextView) view.findViewById(R.id.tvListTranslation)).setText(data.get(i).getRus());
        return view;
    }

}
