package com.costa.androidmobileapp.Listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.costa.androidmobileapp.AdminActivity;
import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.R;
import com.costa.androidmobileapp.Service.RemoteEventServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Costa on 07/01/2018.
 */

public class AdminListener implements View.OnLongClickListener {
    private RemoteEventServiceImpl.RemoteEventServiceInterface remoteEventService = RemoteEventServiceImpl.getInstance();

    Context context;
    String title;

    @Override
    public boolean onLongClick(View view) {

        context = view.getContext();
        TextView EventTitleTextView = (TextView) view.findViewById(R.id.title);

        title = EventTitleTextView.getText().toString().trim();

        final CharSequence[] items = {"Edit", "Delete"};

        new AlertDialog.Builder(context).setTitle("Event")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            editRecord(title);
                        } else if (item == 1) {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }

                            builder.setTitle("Delete Event")
                                    .setMessage("Are you sure you want to delete this Event?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteEvent(title);

                                            ((AdminActivity) context).readRecords();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        dialog.dismiss();

                    }
                }).show();
        return false;
    }

    private void deleteEvent(String title){
        Call<Event> call = remoteEventService.deleteEvent(title);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Toast.makeText(context, "Event was deleted.", Toast.LENGTH_SHORT).show();
                RemoteEventServiceImpl.notifyAllObservers();
                ((AdminActivity) context).readRecords();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(context, "Unable to delete Event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editRecord(final String title) {

        Call<Event> call = remoteEventService.getEvent(title);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                final Event Event = response.body();
                if(Event!=null) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.event_input_form, null, false);



                    final EditText eventNameEditText = (EditText) formElementsView.findViewById(R.id.eventNameEditText);
                    final NumberPicker np = (NumberPicker) formElementsView.findViewById(R.id.numberPickerAdd);
                    np.setMinValue(0);
                    np.setMaxValue(100);
                    np.setWrapSelectorWheel(true);
                    final EditText orgNameEditText = (EditText) formElementsView.findViewById(R.id.orgNameEditText);
                    final EditText locationEditText = (EditText) formElementsView.findViewById(R.id.locationEditText);

                    eventNameEditText.setText(Event.getCardName());
                    orgNameEditText.setText(Event.getNameOrganizor());
                    np.setValue(Event.getNrOfPeople());
                    locationEditText.setText(Event.getLocation());

                    new AlertDialog.Builder(context)
                            .setView(formElementsView)
                            .setTitle("Edit Record")
                            .setPositiveButton("Save Changes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Event Event = new Event(eventNameEditText.getText().toString(),np.getValue(),locationEditText.getText().toString(), orgNameEditText.getText().toString(),1);

                                            Call<Event> call = remoteEventService.createEvent(Event.getCardName(),Event);
                                            call.enqueue(new Callback<Event>() {
                                                @Override
                                                public void onResponse(Call<Event> call, Response<Event> response) {
                                                    ((AdminActivity) context).readRecords();
                                                }

                                                @Override
                                                public void onFailure(Call<Event> call, Throwable t) {

                                                }
                                            });


                                            RemoteEventServiceImpl.notifyAllObservers();
                                            ((AdminActivity) context).readRecords();
                                        }

                                    }).show();
                }
                ((AdminActivity) context).readRecords();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(context, "Unable to edit Event.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
