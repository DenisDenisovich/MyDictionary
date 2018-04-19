package com.dictionary.my.mydictionary.data.db.training.impl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by luxso on 19.04.2018.
 */
public class DBTrainingImplTest {

    DBTrainingImpl db;
    ArrayList<Long> inputLongs = new ArrayList<>();
    ArrayList<Long> expectedOutputLongs = new ArrayList<>();
    @Before
    public void init(){
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        db = new DBTrainingImpl(context);
        for(int i = 5; i < 20; i++){
            inputLongs.add(Long.valueOf(i));
            expectedOutputLongs.add(Long.valueOf(i));
        }
        for(int i = 0; i < 5; i++){
            expectedOutputLongs.add(Long.valueOf(i));
        }
    }

    @Test
    public void engRusWordsTest(){
        db.setEngRusTrainingWords(inputLongs);
        ArrayList<Long> outputLongs = db.getEngRusTrainingWords();

        assertEquals(expectedOutputLongs,outputLongs);
    }
}