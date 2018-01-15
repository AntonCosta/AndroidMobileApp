package com.costa.androidmobileapp;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.costa.androidmobileapp.Listener.AddEventListener;
import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.Service.RemoteEventServiceImpl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Costa on 07/01/2018.
 */

public class AdminActivity extends AppCompatActivity implements Observer {

    private RemoteEventServiceImpl.RemoteEventServiceInterface remoteService = RemoteEventServiceImpl.getInstance();
    private static final String TAG = "AdminActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        Button buttonAdd = (Button) findViewById(R.id.addButton);
        buttonAdd.setOnClickListener(new AddEventListener(remoteService));

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this,MainActivity.class));
                finish();
            }
        });

        RemoteEventServiceImpl.attach(this);

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"Loged in user: "+currentUser.getEmail());

        Button displayChartButton = (Button) findViewById(R.id.displayChart);
        displayChartButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {


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

                            BarChart chart = new BarChart(v.getContext());
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


                /*ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<Event> events = new TableControllerEvents(v.getContext()).read();
                ArrayList<String> labels = new ArrayList<String>();
                int index=-1;
                for (Event obj : events) {
                    index++;
                    entries.add(new BarEntry(obj.getNrOfPeople(),index));
                    labels.add(obj.getCardName());
                }
                BarDataSet dataset = new BarDataSet(entries, "nrOfPeople");

                BarChart chart = new BarChart(v.getContext());
                setContentView(chart);

                BarData data = new BarData(labels, dataset);
                chart.setData(data);

                chart.setDescription("# of rating for each Event");
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart.animateY(5000);*/
            }
        });

        readRecords();
    }

    public void displayData(Map<String,Event> reviews){
        ArrayList<Event> reviewsArray = new ArrayList<>();
        for (Map.Entry<String, Event> entry : reviews.entrySet()) {
            reviewsArray.add(entry.getValue());
        }
        EventAdapter adapter = new EventAdapter(this, reviewsArray, true);
        final ListView listView = findViewById(R.id.ListView);
        listView.setAdapter(adapter);
    }


    public void readRecords() {

        Call<Map<String, Event>> call = remoteService.getAllEvents();
        call.enqueue(new Callback<Map<String, Event>>() {
            @Override
            public void onResponse(Call<Map<String, Event>> call, Response<Map<String, Event>> response) {
                final Map<String, Event> events = response.body();

                if (events != null && !events.isEmpty()) {
                    displayData(events);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Event>> call, Throwable t) {

            }
        });
    }

    @Override
    public void update() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Event Application")
                .setContentText("This list of events has been modified");

        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
    }
}
