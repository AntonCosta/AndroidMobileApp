package com.costa.androidmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.costa.androidmobileapp.Listener.AdminListener;
import com.costa.androidmobileapp.Model.Event;

import java.util.ArrayList;

/**
 * Created by Costa on 07/01/2018.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    private boolean isAdmin;

    public EventAdapter(Context context, ArrayList<Event> reviews, boolean isAdmin){
        super(context,0,reviews);
        this.isAdmin=isAdmin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Event event = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_admin_item,parent,false);
        }

        final TextView reviewTitle = (TextView) convertView.findViewById(R.id.title);
        TextView reviewText = (TextView) convertView.findViewById(R.id.text);
        TextView reviewId = (TextView) convertView.findViewById(R.id.id);

        reviewText.setText(event.getCardName());
        reviewTitle.setText(event.getCardName());
        //reviewId.setText(event.getId());

        if(isAdmin==true){
            convertView.setOnLongClickListener(new AdminListener());

        }
        return convertView;
    }
}
