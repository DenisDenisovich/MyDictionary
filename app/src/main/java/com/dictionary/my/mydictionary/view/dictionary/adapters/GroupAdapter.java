package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

/**
 * Created by luxso on 24.03.2018.
 */

public class GroupAdapter extends ArrayAdapter{

    private ArrayList<Group> data;

    public GroupAdapter(@NonNull Context context, int resource, @NonNull String[] objects, ArrayList<Group> data) {
        super(context, resource, objects);
        this.data = data;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}
