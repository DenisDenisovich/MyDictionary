package com.dictionary.my.mydictionary.presenter.statistic;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.dictionary.my.mydictionary.data.CBKeys;
import com.dictionary.my.mydictionary.data.DataBaseManager;
import com.dictionary.my.mydictionary.view.statistic.ViewStatistic;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by luxso on 31.05.2018.
 */

public class PresenterStatisticImpl<V extends ViewStatistic> implements PresenterStatistic<V>{
    private final static String LOG_TAG = "Log_PresenterStatistic";
    private Database db;
    private V view;
    private Map<String,String> partOfSpeechCodes;
    private Map<String,Integer> partOfSpeechCount;
    Date currentDate;
    Calendar oneWeekAgo;
    int oneWeekCount;
    Calendar twoWeekAgo;
    int twoWeekCount;
    Calendar threeWeekAgo;
    int threeWeekCount;
    Calendar fourWeekAgo;
    int fourWeekCount;
    //ArrayList<Map<String, Integer>> wordsInWeek;
    ArrayList<String> weeks;
    ArrayList<Integer> counts;
    int countWordsInMonth;
    int countAllWords;


    public PresenterStatisticImpl(Context context){
        db = DataBaseManager.getSharedInstance(context).databaseWords;
        partOfSpeechCodes = new HashMap<>();
        partOfSpeechCodes.put("n","noun");
        partOfSpeechCodes.put("v","verb");
        partOfSpeechCodes.put("j","adjective");
        partOfSpeechCodes.put("r", "adverb");
        partOfSpeechCodes.put("prp", "preposition");
        partOfSpeechCodes.put("prn", "pronoun");
        partOfSpeechCodes.put("crd", "cardinal number");
        partOfSpeechCodes.put("cjc", "conjunction");
        partOfSpeechCodes.put("exc",  "interjection");
        partOfSpeechCodes.put("det", "article");
        partOfSpeechCodes.put("abb", "abbreviation");
        partOfSpeechCodes.put("x", "particle");
        partOfSpeechCodes.put("ord", "ordinal number");
        partOfSpeechCodes.put("md", "modal verb");
        partOfSpeechCodes.put("ph", "phrase");
        partOfSpeechCodes.put("phi", "idiom");
        partOfSpeechCount = new HashMap<>();
        partOfSpeechCount.put("NON",0);
        //wordsInWeek = new ArrayList<>();
        currentDate = new Date();
        oneWeekAgo = Calendar.getInstance();
        oneWeekAgo.setTime(currentDate);
        oneWeekAgo.add(Calendar.WEEK_OF_YEAR, -1);
        oneWeekCount = 0;
        twoWeekAgo = Calendar.getInstance();
        twoWeekAgo.setTime(currentDate);
        twoWeekAgo.add(Calendar.WEEK_OF_YEAR, -2);
        twoWeekCount = 0;
        threeWeekAgo = Calendar.getInstance();
        threeWeekAgo.setTime(currentDate);
        threeWeekAgo.add(Calendar.WEEK_OF_YEAR, -3);
        threeWeekCount = 0;
        fourWeekAgo = Calendar.getInstance();
        fourWeekAgo.setTime(currentDate);
        fourWeekAgo.add(Calendar.WEEK_OF_YEAR, -4);
        fourWeekCount = 0;
        countWordsInMonth = 0;
        countAllWords = 0;
        /*Log.d(LOG_TAG, "currentDate " + currentDate.toString());
        Log.d(LOG_TAG, "oneWeekAgo " + oneWeekAgo.getTime().toString());
        Log.d(LOG_TAG, "twoWeekAgo " + twoWeekAgo.getTime().toString());
        Log.d(LOG_TAG, "theeWeekAgo " + threeWeekAgo.getTime().toString());
        Log.d(LOG_TAG, "twoWeekAgo " + fourWeekAgo.getTime().toString());*/
    }
    @Override
    public void attach(V view) {
    this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init() {
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(db));
        try {
            ResultSet rs = query.execute();
            for(Result r:rs){
                countAllWords++;
                Dictionary doc = r.getDictionary(CBKeys.DB_WORDS);
                String pOS = doc.getString(CBKeys.KEY_PART_OF_SPEECH);
                if(pOS != null){
                    String pOSName = partOfSpeechCodes.get(pOS);
                    Log.d(LOG_TAG, pOSName);
                    if( partOfSpeechCount.containsKey(pOSName)){
                        Integer count = partOfSpeechCount.get(pOSName);
                        count++;
                        partOfSpeechCount.put(pOSName,count);
                    }else{
                        partOfSpeechCount.put(pOSName,1);
                    }
                }else{
                    Log.d(LOG_TAG, "pOSName is null");
                    Integer count = partOfSpeechCount.get("NON");
                    count++;
                    partOfSpeechCount.put("NON",count);
                }
                Date wordDate = doc.getDate(CBKeys.KEY_DATE);
                if(!wordDate.before(fourWeekAgo.getTime())){
                    countWordsInMonth++;
                    if(wordDate.before(threeWeekAgo.getTime())){
                        fourWeekCount++;
                    }else if(wordDate.before(twoWeekAgo.getTime())){
                        threeWeekCount++;
                    }else if(wordDate.before(oneWeekAgo.getTime())){
                        twoWeekCount++;
                    }else {
                        oneWeekCount++;
                    }
                }
            }
        }catch (CouchbaseLiteException exc){
            exc.printStackTrace();
        }
        weeks = new ArrayList<>();
        counts = new ArrayList<>();
        view.showCountOfWords(countAllWords);
        view.showPartOfSpeechStatistic(partOfSpeechCount);
        DateFormat dateFormat = new SimpleDateFormat("d MMM yy");
        weeks.add(dateFormat.format(fourWeekAgo.getTime()));
        counts.add(fourWeekCount);
        weeks.add(dateFormat.format(threeWeekAgo.getTime()));
        counts.add(threeWeekCount);
        weeks.add(dateFormat.format(twoWeekAgo.getTime()));
        counts.add(twoWeekCount);
        weeks.add(dateFormat.format(oneWeekAgo.getTime()));
        counts.add(oneWeekCount);
    }

    @Override
    public void setWordsInMonths() {
        String message;
        if(countWordsInMonth < 50){
            message = "low activity";
        } else if( countWordsInMonth < 100){
            message = "medium activity";
        } else{
            message = "high activity";
        }
        view.showWordsInMonthStatistic(weeks,counts,countWordsInMonth, message);
    }

    @Override
    public void setPartOfSpeech() {
        view.showPartOfSpeechStatistic(partOfSpeechCount);
    }

    @Override
    public void update() {

    }
}
