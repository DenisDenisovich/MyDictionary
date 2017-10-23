package com.dictionary.my.mydictionary.view.dictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.presenter.dictionary.DictionaryPresenter;
import com.dictionary.my.mydictionary.presenter.dictionary.DictionaryPresenterImpl;
import com.dictionary.my.mydictionary.view.DictionaryListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by luxso on 19.09.2017.
 */

public class DictionaryView extends Fragment implements Dictionary {
    String[] word = {"Training","Word","Man","Exception","DictionaryView","Table","Apple","Up","Down","World","Note"};
    String[] translate = {"Тренировка","Слово","Человек","Исключение","Словарь","Стол","Яблоко","Верх","Низ","Мир","Тетрадь"};
    String[] from;
    int[] to = {R.id.word_dictionary, R.id.translate_dictionary};
    int selectedPosition = 0;
    String newWord;
    String newTranslate;
    DialogFragment dialog;
    final int REQUEST_CODE = 1;
    ListView listView;
    SimpleAdapter simpleAdapter;
    DictionaryPresenter presenter;
    DictionaryListener mListener;
    long dictionaryId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (DictionaryListener) context;
            dictionaryId = mListener.getDictionary();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new DictionaryPresenterImpl(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attach(this);
        presenter.init(dictionaryId);
        View view = inflater.inflate(R.layout.dictionary_fragment,null);
        listView = (ListView) view.findViewById(R.id.lvDictionary);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.dictionaryFab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        ArrayList<Map<String, String>> data = new ArrayList<>();
        String[] from = {"word", "translate"};
        int[] to = {R.id.word_dictionary, R.id.translate_dictionary};
        for(int i = 0; i < word.length; i++){
            Map<String, String> item = new HashMap<>();
            item.put(from[0],word[i]);
            item.put(from[1],translate[i]);
            data.add(item);
            Log.d("MyLOG", item.toString());
        }

        simpleAdapter = new SimpleAdapter(getActivity(),data,R.layout.dictionary_item,from,to);
        listView.setAdapter(simpleAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void showDictionary(ArrayList<Map<String, Object>> data) {
        simpleAdapter = new SimpleAdapter(getActivity(),data,R.layout.dictionary_item,from,to);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
            }
        });

    }

    @Override
    public void setFromKeys(String... key) {
        /*int lenght = key.length;
        from = new String[lenght];
        for(int i = 0; i < lenght; i++){
            from[i] = key[i];
        }*/
        from = key;
    }

    @Override
    public ArrayList<Long> getDeletedWords() {
        return null;
    }

    @Override
    public Map<String, Object> getNewWord() {
        Map<String, Object> item = new HashMap<>();
        if(newWord != null && newTranslate != null) {
            item.put(from[0], newWord);
            item.put(from[1], newTranslate);
            newWord = null;
            newTranslate = null;
            Toast.makeText(getActivity(),item.toString(),Toast.LENGTH_SHORT).show();
        }
        return item;
    }


    private void createDialog(){
        dialog = new AddWordDialog();
        dialog.setTargetFragment(this,REQUEST_CODE);
        dialog.show(getFragmentManager(),null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE:
                    newWord = data.getStringExtra(from[0]);
                    newTranslate = data.getStringExtra(from[1]);
                    presenter.newWord();
                    break;
            }
        }
    }
}
