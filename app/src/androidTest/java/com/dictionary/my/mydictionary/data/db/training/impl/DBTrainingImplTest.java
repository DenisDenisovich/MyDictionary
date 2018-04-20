package com.dictionary.my.mydictionary.data.db.training.impl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Test for DBTrainingImpl class
 */
@RunWith(Parameterized.class)
public class DBTrainingImplTest {

    DBTrainingImpl db;
    @Parameterized.Parameters
    public static Collection<Object[]> numbers(){
        ArrayList<Long> set = new ArrayList<>();
        ArrayList<Long> del = new ArrayList<>();
        ArrayList<Long> exp = new ArrayList<>();
        del.add(5L);
        del.add(7L);
        del.add(9L);
        for(int i = 0; i < 20; i++){
            exp.add(Long.valueOf(i));
            set.add(Long.valueOf(i));
        }
        exp.remove(5L);
        exp.remove(7L);
        exp.remove(9L);

        ArrayList<Long> set2 = new ArrayList<>(exp);
        ArrayList<Long> del2 = new ArrayList<>(del);
        del2.add(11L);
        del2.add(13L);
        del2.add(14L);
        ArrayList<Long> exp2 = new ArrayList<>(exp);
        exp2.remove(11L);
        exp2.remove(13L);
        exp2.remove(14L);

        ArrayList<Long> set3 = new ArrayList<>(exp2);
        ArrayList<Long> del3 = new ArrayList<>(del2);
        del3.add(16L);
        del3.add(18L);
        del3.add(19L);
        ArrayList<Long> exp3 = new ArrayList<>(exp2);
        exp3.remove(16L);
        exp3.remove(18L);
        exp3.remove(19L);

        ArrayList<Long> set4 = new ArrayList<>(exp3);
        ArrayList<Long> del4 = new ArrayList<>(del3);
        del4.add(0L);
        del4.add(1L);
        del4.add(2L);
        del4.add(3L);
        del4.add(4L);
        del4.add(6L);
        del4.add(8L);
        del4.add(10L);
        del4.add(12L);
        del4.add(15L);
        del4.add(17L);

        ArrayList<Long> exp4 = new ArrayList<>();
        return Arrays.asList(new Object[][]{
                {set, del,exp},
                {set2,del2,exp2},
                {set3,del3,exp3},
                {set4,del4,exp4}
        });
    }

    ArrayList<Long> set = null;
    ArrayList<Long> del = null;
    ArrayList<Long> exp = null;
    public DBTrainingImplTest(ArrayList<Long> set, ArrayList<Long> del, ArrayList<Long> exp){
        this.set = set;
        this.del = del;
        this.exp = exp;
    }

    @Before
    public void init(){
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        db = new DBTrainingImpl(context);
    }

    @Test
    public void setWordsToEngRusTraining(){
        db.setWordsToTraining(set, 1);
        ArrayList<Long> output = db.getWordsFromTraining(1);
        assertEquals(set, output);

    }
    @Test
    public void deleteWordsFromTraining(){
        db.setWordsToTraining(set,1);
        db.deleteWordsFromTraining(del,1);
        ArrayList<Long> output  = db.getWordsFromTraining(1);
        assertEquals(exp,output);
    }
    @Test
    public void getWordsFromTraining(){
        db.setWordsToTraining(set,1);
        db.deleteWordsFromTraining(del,1);
        ArrayList<Long> output = db.getWordsFromTraining(1);
        assertEquals(exp, output);
    }
    @Test
    public void getCountOfWordsFromTraining(){
        db.setWordsToTraining(set, 1);
        db.deleteWordsFromTraining(del, 1);
        Integer count  = db.getWordsFromTraining(1).size();
        Integer expCount = exp.size();
        assertEquals(expCount,count);

    }
}