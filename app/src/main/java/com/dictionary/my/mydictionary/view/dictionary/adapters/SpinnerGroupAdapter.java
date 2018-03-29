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
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

/**
 * Created by luxso on 24.03.2018.
 */

public class SpinnerGroupAdapter extends ArrayAdapter{

    private ArrayList<Group> data;
    private Context context;
    private int resource;

    public SpinnerGroupAdapter(@NonNull Context context, int resource, @NonNull String[] objects, ArrayList<Group> data) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light);
        if(convertView == null){
            convertView = View.inflate(context, resource, null);
        }
        ((TextView)convertView).setText(data.get(position).getTitle());
        ((TextView)convertView).setTypeface(typeface);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getDropDownView(position, convertView, parent);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light);
        if(convertView == null){
            convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
        }

        ((TextView)convertView).setText(data.get(position).getTitle());
        ((TextView)convertView).setTypeface(typeface);
        int paddingTop = (int)context.getResources().getDimension(R.dimen.dropdown_top_padding);
        int paddingBottom = (int)context.getResources().getDimension(R.dimen.dropdown_bottom_padding);
        int paddingLeft = (int)context.getResources().getDimension(R.dimen.dropdown_left_padding);
        int paddingRight = (int)context.getResources().getDimension(R.dimen.dropdown_right_padding);
        ((TextView)convertView).setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}
