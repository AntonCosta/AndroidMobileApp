package com.costa.androidmobileapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.Service.RemoteEventServiceImpl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Costa on 20/12/2017.
 */

public class Statistics extends AppCompatActivity {

    private RemoteEventServiceImpl.RemoteEventServiceInterface remoteService = RemoteEventServiceImpl.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context;
        context = getApplicationContext();

        Call<Map<String, Event>> call = remoteService.getAllEvents();
        call.enqueue(new Callback<Map<String, Event>>() {
            @Override
            public void onResponse(Call<Map<String, Event>> call, Response<Map<String, Event>> response) {
                final Map<String, Event> events = response.body();
                ArrayList<String> labels = new ArrayList<String>();
                ArrayList<BarEntry> entries = new ArrayList<>();

                if (events != null && !events.isEmpty()) {
                    int index=-1;
                    for (Event obj : events.values()) {
                        index++;
                        entries.add(new BarEntry(obj.getNrOfPeople(),index));
                        labels.add(obj.getCardName());
                    }
                    BarDataSet dataset = new BarDataSet(entries, "nrOfPeople");

                    BarChart chart = new BarChart(context);
                    setContentView(chart);

                    BarData data = new BarData(labels, dataset);
                    chart.setData(data);

                    chart.setDescription("# of rating for each Event");
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    chart.animateY(5000);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Event>> call, Throwable t) {

            }
        });




       /* ArrayList<BarEntry> entries = new ArrayList<>();
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
        chart.animateY(5000);*/
    }
}
