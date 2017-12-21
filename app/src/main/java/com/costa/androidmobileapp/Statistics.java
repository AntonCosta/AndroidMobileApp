package com.costa.androidmobileapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.costa.androidmobileapp.Model.Event;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Costa on 20/12/2017.
 */

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Context context;
        context = getApplicationContext();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Event> events = new TableControllerEvents(context).read();
        ArrayList<String> labels = new ArrayList<String>();
        int index=-1;
        for (Event obj : events) {
            index++;
            entries.add(new BarEntry(obj.getNrOfPeople(),index));
            labels.add(obj.getCardName());
        }
        BarDataSet dataset = new BarDataSet(entries, "events");

        BarChart chart = new BarChart(context);
        setContentView(chart);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);

        chart.setDescription("# of people for each event");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.animateY(5000);
    }
}
