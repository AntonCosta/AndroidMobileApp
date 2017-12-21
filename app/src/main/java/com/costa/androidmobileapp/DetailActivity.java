package com.costa.androidmobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.costa.androidmobileapp.Model.Event;

public class DetailActivity extends AppCompatActivity {

    TextView nameTxt;
    ImageView img;
    int position;
    int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);

        nameTxt = findViewById(R.id.nameTxtDetail);
        img = findViewById(R.id.eventImageDetail);

        //RECEIVE
        Intent i = this.getIntent();
        String name = i.getExtras().getString("TITLE_KEY");
        Integer imgs = i.getExtras().getInt("TAG_KEY");
        position = i.getExtras().getInt("POSITION_KEY");
        eventId = i.getExtras().getInt("EVENT_ID");

        //BIND
        nameTxt.setText(name);
        img.setImageResource(imgs);
        img.setTag(imgs);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void updateBtnClick(View view) {

        Context context = view.getContext();
        final TableControllerEvents tableControllerEvents = new TableControllerEvents(view.getContext());
        EditText updateNameTextField = findViewById(R.id.updateText);
        Event event = new Event(eventId,updateNameTextField.getText().toString(),0,"",
                "",0);

        boolean updateSuccessful = tableControllerEvents.update(event);

        if(updateSuccessful){
            Toast.makeText(context, "Event record was updated.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Unable to update event.", Toast.LENGTH_SHORT).show();
        }

       // EditText updateNameTextField = findViewById(R.id.updateText);

        //String title = updateNameTextField.getText().toString();


        Intent intent = new Intent(this, Navigation.class);
        /*intent.putExtra("TITLE_KEY", title);
        intent.putExtra("POSITION_KEY", position);*/
        startActivity(intent);

        //PACK DATA
    }

    public void deleteBtnClick(View view)
    {
        final Context context = view.getContext();
        final Intent intent = new Intent(this, Navigation.class);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Delete event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        boolean deleteSuccessful = new TableControllerEvents(context).delete(eventId);

                        if (deleteSuccessful) {
                            Toast.makeText(context, "Event was deleted.", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Unable to delete event.", Toast.LENGTH_SHORT).show();
                        }
                        }
                    })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();



    }


    public void mailBtnClick(View view)
    {

        TextView eventName = findViewById(R.id.nameTxtDetail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //   final Intent intent = new Intent(this, ContactUsActivity.class);

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Event Information");
        intent.putExtra(Intent.EXTRA_TEXT, eventName.getText().toString());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

}
