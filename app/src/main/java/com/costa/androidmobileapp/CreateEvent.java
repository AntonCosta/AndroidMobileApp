package com.costa.androidmobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.costa.androidmobileapp.Model.Event;

/**
 * Created by Costa on 17/12/2017.
 */

public class CreateEvent extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_input_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NumberPicker np = (NumberPicker) findViewById(R.id.numberPickerAdd);
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);
    }

    public void createBtnClick(View view)
    {
        Context context;
        context = view.getContext();

        final EditText eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        final NumberPicker np = (NumberPicker) findViewById(R.id.numberPickerAdd);
        final EditText orgNameEditText = (EditText) findViewById(R.id.orgNameEditText);
        final EditText locationEditText = (EditText) findViewById(R.id.locationEditText);

        String eventName = eventNameEditText.getText().toString();
        String orgName = orgNameEditText.getText().toString();
        String locationName = locationEditText.getText().toString();

        final Event newEvent  =new Event(eventName, np.getValue(), orgName, locationName,1);

        boolean createSuccessful = new TableControllerEvents(context).create(newEvent);

        if (createSuccessful) {
            Toast.makeText(context, "Event was added.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Unable to add event information.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Navigation.class);
        startActivity(intent);
        super.onBackPressed();

    }
}
