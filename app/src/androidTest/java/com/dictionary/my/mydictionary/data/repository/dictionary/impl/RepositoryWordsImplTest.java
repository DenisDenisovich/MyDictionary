package com.dictionary.my.mydictionary.data.repository.dictionary.impl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.dictionary.my.mydictionary.data.db.training.impl.DBTrainingImpl;
import com.dictionary.my.mydictionary.data.repository.dictionary.RepositoryWords;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by luxso on 20.04.2018.
 */
@RunWith(Parameterized.class)
public class RepositoryWordsImplTest {
    RepositoryWords repository;
    DBTrainingImpl db;
    @Before
    public void init(){
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        repository = new RepositoryWordsImpl(context);
        db = new DBTrainingImpl(context);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> numbers(){
        ArrayList<Long> set = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            set.add(Long.valueOf(i));
        }
        ArrayList<Long> exp = new ArrayList<>(set);

        ArrayList<Long> set2 = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            set2.add(Long.valueOf(i));
        }
        ArrayList<Long> exp2 = new ArrayList<>(set);

        ArrayList<Long> set3 = new ArrayList<>();
        for(int i = 5; i < 10; i++){
            set3.add(Long.valueOf(i));
        }
        ArrayList<Long> exp3 = new ArrayList<>(set3);
        for(int i = 0; i < 5; i++){
            exp3.add(Long.valueOf(i));
        }
        for(int i = 10; i < 20; i++){
            exp3.add(Long.valueOf(i));
        }

        ArrayList<Long> set4 = new ArrayList<>();
        ArrayList<Long> exp4 = new ArrayList<>(exp3);
        ArrayList<Long> set5 = new ArrayList<>(set);
        set5.addAll(set);
        return Arrays.asList(new Object[][]{
                {set,exp},
                {set2,exp2},
                {set3,exp3},
                {set4,exp4},
                {set5,exp}
        });
    }

    ArrayList<Long> set = null;
    ArrayList<Long> exp = null;
    public RepositoryWordsImplTest(ArrayList<Long> set, ArrayList<Long> exp){
        this.set = set;
        this.exp = exp;
    }

    @Test
    public void getListOfTrainings() throws Exception {
        ArrayList<Long> expectedList = new ArrayList<>();
        expectedList.add(1L);
        expectedList.add(2L);
        expectedList.add(3L);
        expectedList.add(4L);
        expectedList.add(5L);
        repository.getListOfTrainings()
                .test()
                .assertResult(expectedList);
    }

    @Test
    public void setWordsToTraining() throws Exception {
        repository.setWordsToTraining(set,1)
                .test()
                .assertComplete();
        ArrayList<Long> output = db.getWordsFromTraining(1);
        assertEquals(exp,output);
    }

    @Test
    public void setWordsToTrainingWithNullPointException(){
        db.setWordsToTraining(set,1);
        db.deleteWordsFromTraining(set,1);
        repository.setWordsToTraining(null,1)
                .test()
                .assertError(NullPointerException.class);
    }

    @Test
    public void setWordsToTrainingWithIndexOutOfBoundsException(){
        db.setWordsToTraining(set,1);
        db.deleteWordsFromTraining(set,1);
        repository.setWordsToTraining(new ArrayList<>(),1)
                .test()
                .assertError(IndexOutOfBoundsException.class);
    }

    @Test
    public void setWordsToTrainingWithSimpleValue(){
        db.setWordsToTraining(set,1);
        set.remove(19L);
        db.deleteWordsFromTraining(set,1);
        repository.setWordsToTraining(new ArrayList<>(),1)
                .test()
                .assertComplete();
        ArrayList<Long> output = db.getWordsFromTraining(1);
        ArrayList<Long> l = new ArrayList<>();
        l.add(19L);
        assertEquals(l,output);
    }



}