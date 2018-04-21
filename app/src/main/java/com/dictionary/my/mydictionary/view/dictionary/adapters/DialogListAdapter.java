package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;

import java.util.ArrayList;

/**
 * Created by luxso on 21.04.2018.
 */

public class DialogListAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private long[] ids;
    private String[] titles;

    public DialogListAdapter(@NonNull Context context, int resource, @NonNull String[] objects, long[] ids) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.ids = ids;
        this.titles = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, resource, null);
        }
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light);
        ((TextView) convertView).setText(titles[position]);
        ((TextView)convertView).setTypeface(typeface);
        int paddingTop = (int)context.getResources().getDimension(R.dimen.dropdown_top_padding);
        int paddingBottom = (int)context.getResources().getDimension(R.dimen.dropdown_bottom_padding);
        convertView.setPadding(0,paddingTop,0,paddingBottom);
        return super.getView(position, convertView, parent);
    }

    @Override
    public long getItemId(int position) {
        return ids[position];
    }

    public String getItemTitle(int position){
        return titles[position];
    }
}
