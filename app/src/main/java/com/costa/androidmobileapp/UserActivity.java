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

import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.Service.RemoteEventServiceImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Costa on 07/01/2018.
 */

public class UserActivity extends AppCompatActivity implements Observer {

    private RemoteEventServiceImpl.RemoteEventServiceInterface remoteService = RemoteEventServiceImpl.getInstance();
    private static final String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserActivity.this,MainActivity.class));
                finish();
            }
        });

        RemoteEventServiceImpl.attach(this);

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"Loged in user: "+currentUser.getEmail());


        readRecords();
    }

    public void displayData(Map<String,Event> events){
        ArrayList<Event> eventsArray = new ArrayList<>();
        for (Map.Entry<String, Event> entry : events.entrySet()) {
            eventsArray.add(entry.getValue());
        }
        EventAdapter adapter = new EventAdapter(this, eventsArray, false);
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
