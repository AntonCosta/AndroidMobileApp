package com.costa.androidmobileapp;

/**
 * Created by CristianCosmin on 08.11.2017.
 */

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import com.costa.androidmobileapp.Listener.ItemClickListener;
import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.Service.RemoteEventServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;


public class CardFragment extends Fragment implements  Observer{

    ArrayList<Event> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Events[] = {"Library", "Morning Cofee", "Birthday Party", "Gym night", "Concert Flying", "Quiz Londoner"};
    int Images[] = {R.drawable.book, R.drawable.coffee_cup_md, R.drawable.party_popper, R.drawable.weight_lifting, R.drawable.flying_circus, R.drawable.londoner};
    private RemoteEventServiceImpl.RemoteEventServiceInterface remoteService = RemoteEventServiceImpl.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getIntent().getExtras() != null) {
            String titleToChange = getActivity().getIntent().getExtras().getString("TITLE_KEY");
            Integer posAtWhichToChange = getActivity().getIntent().getExtras().getInt("POSITION_KEY");
            if (titleToChange != null && posAtWhichToChange != null) {
                Events[posAtWhichToChange] = titleToChange;
            }
        }

        initializeList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems, this.getContext()));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    public void displayData(ArrayList<Event> events){


        ArrayList<Event> eventsArray = new ArrayList<>();
        for (Event entry : events ) {
            eventsArray.add(entry);
        }
        EventAdapter adapter = new EventAdapter(this.getContext(), eventsArray, false);
        final RecyclerView MyRecyclerView = (RecyclerView) getView().findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems, this.getContext()));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Event> list;
        Context c;

        public MyAdapter(ArrayList<Event> Data, Context c) {
            this.c = c;
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final Event event = list.get(position);
            final int positionToSent = position;

            holder.titleTextView.setText(event.getCardName());
            holder.coverImageView.setImageResource(event.getImageResourceId());
            holder.coverImageView.setTag(event.getImageResourceId());
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick() {
                    openDetailActivity(event.getCardName(), event.getImageResourceId(), position, event.getIdEvent());
                }
            });
        }

        public void openDetailActivity(String title, Integer tag, int position, int eventId) {
            Intent i = new Intent(c, DetailActivity.class);

            //PACK DATA
            i.putExtra("TITLE_KEY", title);
            i.putExtra("TAG_KEY", tag);
            i.putExtra("POSITION_KEY", position);
            i.putExtra("EVENT_ID", eventId);

            c.startActivity(i);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public ImageView coverImageView;
        ItemClickListener itemClickListener;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick();
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }


    public void initializeList() {

        Call<Map<String, Event>> call = remoteService.getAllEvents();
        call.enqueue(new Callback<Map<String, Event>>() {
            @Override
            public void onResponse(Call<Map<String, Event>> call, Response<Map<String, Event>> response) {
                final Map<String, Event> events = response.body();

                if (events != null && !events.isEmpty()) {
                    for (Event obj : events.values()) {

                        int id = obj.getIdEvent();
                        String eventName = obj.getCardName();
                        int nrOfPeople = obj.getNrOfPeople();
                        String orgName = obj.getNameOrganizor();
                        String location = obj.getLocation();
                        int imageId = obj.getImageResourceId();

                        // To change after added image dbs, maybe

                        obj.setImageResourceId(Images[obj.getImageResourceId()]);
                        listitems.add(obj);

                    }
                  //  displayData(listitems);

                }
            }

            @Override
            public void onFailure(Call<Map<String, Event>> call, Throwable t) {

            }

        });


        /*ArrayList<Event> events = new TableControllerEvents(this.getContext()).read();
        listitems.clear();
        for (Event obj : events) {

            int id = obj.getIdEvent();
            String eventName = obj.getCardName();
            int nrOfPeople = obj.getNrOfPeople();
            String orgName = obj.getNameOrganizor();
            String location = obj.getLocation();
            int imageId = obj.getImageResourceId();

            // To change after added image dbs, maybe

            obj.setImageResourceId(Images[obj.getImageResourceId()]);
            listitems.add(obj);
         }

      *//*  for (int i = 0; i < 6; i++) {
            Event item = new Event();
            item.setCardName(Events[i]);
            item.setImageResourceId(Images[i]);
            listitems.add(item);
        }*//*
    }*/
    }

    @Override
    public void update() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getContext())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Event Application")
                .setContentText("This list of events has been modified");

        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
    }
}
