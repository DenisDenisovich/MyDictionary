package com.dictionary.my.mydictionary.view.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;
import com.dictionary.my.mydictionary.presenter.statistic.PresenterStatistic;
import com.dictionary.my.mydictionary.presenter.statistic.PresenterStatisticImpl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by luxso on 31.05.2018.
 */

public class StatisticFragment extends Fragment implements ViewStatistic{
    private final static String LOG_TAG = "Log_ SFragment";
    PieChart pieChart;
    PresenterStatistic presenter;
    PieData pieData;
    //HorizontalBarChart barChart;
    BarChart barChart;
    TextView tvCountOfWords;
    TextView tvCountOfWordsInMonth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic,null);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        tvCountOfWords = view.findViewById(R.id.countWords);
        tvCountOfWordsInMonth = view.findViewById(R.id.tvCountInMonth);
        presenter = new PresenterStatisticImpl(getContext());
        presenter.attach(this);
        presenter.init();
        view.findViewById(R.id.btnPieChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setPartOfSpeech();
            }
        });
        view.findViewById(R.id.btnChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setWordsInMonths();
            }
        });
        return view;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showERROR(String message) {

    }

    @Override
    public void showCountOfWords(Integer count) {
        tvCountOfWords.setText("Count of words: " + String.valueOf(count));
    }

    @Override
    public void showPartOfSpeechStatistic(Map<String, Integer> partOfSpeech) {
        Log.d(LOG_TAG, "partOfSpeech size: " + partOfSpeech.size());
        pieChart.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);
        tvCountOfWordsInMonth.setVisibility(View.GONE);
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Pie chart of Parts of Speech");
        description.setTextSize(12);
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        ArrayList<PieEntry> ydata = new ArrayList<>();
        Set<String> keys = partOfSpeech.keySet();
        Log.d(LOG_TAG, "count: " + keys.size());
        for (String s: keys){
            Log.d(LOG_TAG, "key: " + s + " - " + (Integer) partOfSpeech.get(s));
            ydata.add(new PieEntry((Integer) partOfSpeech.get(s), s));
        }
        PieDataSet pieDataSet = new PieDataSet(ydata, "Part Of Speech");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);
        colors.add(Color.MAGENTA);
        colors.add(Color.YELLOW);
        colors.add(Color.rgb(249, 133, 255));
        colors.add(Color.rgb(100, 100, 70));
        colors.add(Color.rgb(50, 133, 255));
        colors.add(Color.rgb(0, 133, 255));
        colors.add(Color.rgb(249, 33, 36));
        colors.add(Color.rgb(98, 235, 78));
        colors.add(Color.rgb(4, 96, 5));
        colors.add(Color.rgb(249, 100, 100));
        colors.add(Color.rgb(249, 0, 255));
        colors.add(Color.rgb(249, 133, 0));
        //pieDataSet.setColors(colors);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);


        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.setCenterText(String.valueOf((int)Math.round(e.getY())) + " words");
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public void showWordsInMonthStatistic(ArrayList<String> weeks, ArrayList<Integer> counts, Integer countWordsOfMonth, String message) {
        pieChart.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);
        tvCountOfWordsInMonth.setVisibility(View.VISIBLE);
        tvCountOfWordsInMonth.setText("Count of words in month: " + String.valueOf(countWordsOfMonth) + " - " + message);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0.5f,counts.get(0)));
        entries.add(new BarEntry(1.5f,counts.get(1)));
        entries.add(new BarEntry(2.5f,counts.get(2)));
        entries.add(new BarEntry(3.5f,counts.get(3)));
        BarDataSet bardataset = new BarDataSet(entries, "Count of words in week");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(bardataset);


        Description description = new Description();
        description.setText("Bar chart of words in month");
        description.setTextSize(12);
        barChart.setDescription(description);
        barChart.setData(barData);
        XAxis axis = barChart.getXAxis();
        axis.setValueFormatter(new MyXAxisForm(weeks));
        axis.setAxisMinimum(0f);
        axis.setGranularity(1f);
        axis.setAxisMaximum(3.99f);
        //axis.setAvoidFirstLastClipping(true);
        //axis.setCenterAxisLabels(true);
        barChart.setFitBars(true);
        barChart.invalidate();

    }
    public class MyXAxisForm implements IAxisValueFormatter{
        private String[] weeks;
        public MyXAxisForm(ArrayList<String> weeks){
            this.weeks = weeks.toArray(new String[0]);
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return weeks[(int)value];
        }
    }
}
