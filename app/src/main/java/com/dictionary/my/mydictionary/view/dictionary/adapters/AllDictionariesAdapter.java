package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private int resource;
    private String[] from;
    private int[] to;
    public AllDictionariesAdapter(Context context, ArrayList<Map<String,Object>> data, int resource, String[] from, int[] to){
        this.context = context;
        this.data = data;
        this.resource = resource;
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
            view = layoutInflater.inflate(resource,null);
        }
        if(deleteList.contains(getItemId(i))){
            (view.findViewById(R.id.dictionaryItem)).setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryLight));
        }else{
            (view.findViewById(R.id.dictionaryItem)).setBackgroundColor(ContextCompat.getColor(context,R.color.colorDictionaryItem));
        }
        ((TextView) view.findViewById(to[0])).setText(getText(i));
        return view;
    }

    private String getText(int i){
        return (String)data.get(i).get(from[1]);
    }

    public int addViewToDeleteList(long l){
        deleteList.add(l);
        this.notifyDataSetChanged();
        return deleteList.size();
    }

    public boolean viewIsDelete(long l){
        if(deleteList.contains(l)){
            return true;
        }
        return false;
    }

    public int removeViewFromDeleteList(long l){
        deleteList.remove(l);
        this.notifyDataSetChanged();
        return deleteList.size();
    }

    public ArrayList<Long> getDeleteList(){
        return deleteList;
    }

    public Integer getSizeOfDeleteList(){
        this.notifyDataSetChanged();
        return deleteList.size();
    }

    public int clearDeleteList(){
        deleteList.clear();
        this.notifyDataSetChanged();
        return deleteList.size();
    }

    public Map<String,Object> getEditItem(){
        Map<String, Object> item = null;
        if(deleteList.size() == 1) {
            long l = deleteList.get(0);
            for(int i = 0; i < data.size(); i++){
                item = data.get(i);
                if((Long)item.get(from[0]) == l){
                    break;
                }
            }
        }
        return item;
    }

}
