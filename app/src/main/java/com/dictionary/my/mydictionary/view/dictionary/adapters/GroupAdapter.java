package com.dictionary.my.mydictionary.view.dictionary.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.domain.entites.dictionary.Group;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by luxso on 27.03.2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{
    private final static String LOG_TAG = "Log_WordAdapter";


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvGroup;
        public ImageButton checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            checkBox = itemView.findViewById(R.id.checkBoxGroup);
        }
    }

    private ArrayList<Group> mdata;
    private ArrayList<Long> selectedItemIds;
    private PublishSubject<Integer> countOfSelectObservable;
    private PublishSubject<Group> choiceGroupObservable;
    private boolean selectMode = false;
    private Context context;
    public GroupAdapter(Context context, ArrayList<Group> data){
        this.context = context;
        mdata = data;
        selectedItemIds = new ArrayList<>();
        countOfSelectObservable = PublishSubject.create();
        choiceGroupObservable = PublishSubject.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if(selectMode){
            int marginTop = (int)context.getResources().getDimension(R.dimen.llWord_top_margin);
            int marginStart = (int)context.getResources().getDimension(R.dimen.llWord_start_margin_selected);
            int marginBottom = (int)context.getResources().getDimension(R.dimen.llWord_bottom_margin);
            lp.setMargins(marginStart,marginTop,0,marginBottom);
            lp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            holder.itemView.findViewById(R.id.llGroups).setLayoutParams(lp);

            holder.checkBox.setVisibility(View.VISIBLE);
            if(selectedItemIds.contains(mdata.get(position).getId())){

                holder.checkBox.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_check_box_selected));
            }else{
                holder.checkBox.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_check_box));
            }

        }else{
            holder.checkBox.setVisibility(View.GONE);
            int marginTop = (int)context.getResources().getDimension(R.dimen.llWord_top_margin);
            int marginStart = (int)context.getResources().getDimension(R.dimen.llWord_start_margin);
            int marginBottom = (int)context.getResources().getDimension(R.dimen.llWord_bottom_margin);
            lp.setMargins(marginStart,marginTop,0,marginBottom);
            lp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            holder.itemView.findViewById(R.id.llGroups).setLayoutParams(lp);
        }
        holder.tvGroup.setText(mdata.get(position).getTitle());

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
                }else{
                    choiceGroupObservable.onNext(mdata.get(position));
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
        countOfSelectObservable.onNext(selectedItemIds.size());
        notifyDataSetChanged();
    }
    public int getSelectedItemsSize(){
        return selectedItemIds.size();
    }

    public PublishSubject<Integer> getcountOfSelectObservable(){
        return countOfSelectObservable;
    }
    public PublishSubject<Group> getChoiceGroupObservable(){
        return choiceGroupObservable;
    }

    public ArrayList<Long> getSelectedItemIds(){
        return selectedItemIds;
    }

    public Group getSelectedGroup(){
        for(int i = 0; i < mdata.size(); i++){
            if(mdata.get(i).getId() == selectedItemIds.get(0)){
                return mdata.get(i);
            }
        }
        return null;
    }

    public void selectModeOff(){
        selectMode = false;
        selectedItemIds.clear();
        notifyDataSetChanged();
    }

    public void deleteSelectedGroups(){
        ArrayList<Group> deleteList = new ArrayList<>();
        int dataSize = mdata.size();
        for(int i = 0; i < mdata.size(); i++){
            if(selectedItemIds.contains(mdata.get(i).getId())){
                deleteList.add(mdata.get(i));
            }
        }
        mdata.removeAll(deleteList);
        selectModeOff();

    }

    public void editSelectedGroup(Group group){
        for(int i = 0; i < mdata.size(); i++){
            if(mdata.get(i).getId() == group.getId()){
                mdata.set(i,group);
            }
        }
        selectModeOff();
    }

}
