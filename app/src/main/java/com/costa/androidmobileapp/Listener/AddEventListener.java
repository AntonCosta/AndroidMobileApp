package com.costa.androidmobileapp.Listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.costa.androidmobileapp.AdminActivity;
import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.R;
import com.costa.androidmobileapp.Service.RemoteEventServiceImpl;
import com.costa.androidmobileapp.TableControllerEvents;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Costa on 07/01/2018.
 */

public class AddEventListener implements View.OnClickListener{

    private RemoteEventServiceImpl.RemoteEventServiceInterface remoteService;
    private static final String TAG = "OnClickListenerAdd";


    public AddEventListener(RemoteEventServiceImpl.RemoteEventServiceInterface service){
        remoteService=service;
    }

    public void onClick(View v) {
        final Context context = v.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.event_input_form, null, false);


        NumberPicker np = (NumberPicker) formElementsView.findViewById(R.id.numberPickerAdd);
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);

        final EditText eventNameEditText = (EditText) formElementsView.findViewById(R.id.eventNameEditText);
        np = (NumberPicker) formElementsView.findViewById(R.id.numberPickerAdd);
        final EditText orgNameEditText = (EditText) formElementsView.findViewById(R.id.orgNameEditText);
        final EditText locationEditText = (EditText) formElementsView.findViewById(R.id.locationEditText);



        /*final Event newEvent  =new Event(eventName, np.getValue(), orgName, locationName,1);

        boolean createSuccessful = new TableControllerEvents(context).create(newEvent);*/

        /*if (createSuccessful) {
            Toast.makeText(context, "Event was added.", Toast.LENGTH_SHORT).show();
        } else {
     /*      *//* Toast.makeText(context, "Unable to add event information.", Toast.LENGTH_SHORT).show();
        }*/


        final NumberPicker finalNp = np;
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Add Event")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String eventName = eventNameEditText.getText().toString();
                                String orgName = orgNameEditText.getText().toString();
                                String locationName = locationEditText.getText().toString();

                                final Event Event = new Event(eventName, finalNp.getValue(), locationName, orgName, 1);//image id is 1 temporarily


                                Call<Event> call = remoteService.createEvent(Event.getCardName(), Event);

                                call.enqueue(new Callback<Event>() {
                                    @Override
                                    public void onResponse(Call<Event> call, Response<Event> response) {
                                        Toast.makeText(context, "Event was added.", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(Intent.ACTION_SEND);
                                        i.setType("message/rfc822");
                                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"costaanton96@gmail.com"});
                                        i.putExtra(Intent.EXTRA_SUBJECT, "Email send from Android Studio");
                                        i.putExtra(Intent.EXTRA_TEXT, Event.toString());
                                        try {
                                            context.startActivity(Intent.createChooser(i, "Send mail..."));
                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                        }
                                        ((AdminActivity) context).readRecords();

                                        RemoteEventServiceImpl.notifyAllObservers();

                                    }

                                    @Override
                                    public void onFailure(Call<Event> call, Throwable t) {
                                        Toast.makeText(context, "Unable to add Event information.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //((AdminActivity) context).readRecords();

                                dialog.cancel();
                            }

                        }).show();
    }
}
