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
 * This class is used for spinner in AllGroupsFragment/AllWordsFragment and in AddWordActivity
 */

public class SpinnerAdapter extends ArrayAdapter{

    private ArrayList<Group> data;
    private String[] lines;
    private Context context;
    private int resource;
    private boolean isGroupList;

    // constructor for AddWordActivity. He gets titles of group(objects) and ArrayList of Groups(data)
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects, ArrayList<Group> data) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = data;
        isGroupList = true;
    }
    // constructor for AllGroupsFragment/AllWordsFragment. He gets titles of AllGroups and AllWords Fragments
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        lines = objects;
        isGroupList = false;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light);
        if(convertView == null){
            convertView = View.inflate(context, resource, null);
        }
        if(isGroupList) {
            ((TextView) convertView).setText(data.get(position).getTitle());
        }else {
            ((TextView) convertView).setText(lines[position]);
        }
        ((TextView)convertView).setTypeface(typeface);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
        }
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light);
        if(isGroupList) {
            ((TextView) convertView).setText(data.get(position).getTitle());
        }else {
            ((TextView) convertView).setText(lines[position]);
        }
        int paddingTop = (int)context.getResources().getDimension(R.dimen.dropdown_top_padding);
        int paddingBottom = (int)context.getResources().getDimension(R.dimen.dropdown_bottom_padding);
        int paddingLeft = (int)context.getResources().getDimension(R.dimen.dropdown_left_padding);
        int paddingRight = (int)context.getResources().getDimension(R.dimen.dropdown_right_padding);
        convertView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        ((TextView) convertView).setTypeface(typeface);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getGroupString(int position){
        return data.get(position).getId();
    }
}
