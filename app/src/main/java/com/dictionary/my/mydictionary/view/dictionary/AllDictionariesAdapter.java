package com.dictionary.my.mydictionary.view.dictionary;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by luxso on 12.10.2017.
 */

public class AllDictionariesAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<Map<String,Object>> data;
    private ArrayList<Long> deleteList;
    private int resourse;
    private String[] from;
    private int[] to;
    public AllDictionariesAdapter(Context context, ArrayList<Map<String,Object>> data, int resourse, String[] from, int[] to){
        this.context = context;
        this.data = data;
        this.resourse = resourse;
        this.from = from;
        this.to = to;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        deleteList = new ArrayList<>();
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
        return (Long)data.get(i).get(from[0]);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = layoutInflater.inflate(resourse,null);
        }
        if(deleteList.contains(getItemId(i))){
            ((LinearLayout) view.findViewById(R.id.dictionaryItem)).setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryLight));
        }else{
            ((LinearLayout) view.findViewById(R.id.dictionaryItem)).setBackgroundColor(ContextCompat.getColor(context,R.color.colorDictionaryItem));
        }
        ((TextView) view.findViewById(to[0])).setText(getText(i));
        return view;
    }

    private String getText(int i){
        return (String)data.get(i).get(from[1]);
    }

    public void addViewToDeleteList(long l){
        deleteList.add(l);
        this.notifyDataSetChanged();
    }

    public boolean viewIsDelete(long l){
        if(deleteList.contains(l)){
            return true;
        }
        return false;
    }

    public void removeViewFromDeleteList(long l){
        deleteList.remove(l);
        this.notifyDataSetChanged();
    }

    public ArrayList<Long> getDeleteList(){
        return deleteList;
    }

    public Integer getSizeOfDeleteList(){
        Log.d("LOG_TAG", deleteList.toString());
        this.notifyDataSetChanged();
        return deleteList.size();
    }

    public void clearDeleteList(){
        deleteList.clear();
        this.notifyDataSetChanged();
    }

}
