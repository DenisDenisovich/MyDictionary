package com.dictionary.my.mydictionary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.dictionary.my.mydictionary.data.Content;
import com.dictionary.my.mydictionary.data.db.training.DBTraining;
import com.dictionary.my.mydictionary.data.db.training.impl.DBTrainingImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Before
    public void init(){
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
    }
    @Test
    public void useAppContext() throws Exception {

    }
}
