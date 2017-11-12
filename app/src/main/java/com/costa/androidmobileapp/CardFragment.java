package com.costa.androidmobileapp;

/**
 * Created by CristianCosmin on 08.11.2017.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.costa.androidmobileapp.Model.Event;


public class CardFragment extends Fragment {

    ArrayList<Event> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Events[] = {"Library", "Morning Cofee", "Birthday Party", "Gym night", "Concert Flying", "Quiz Londoner"};
    int Images[] = {R.drawable.book, R.drawable.coffee_cup_md, R.drawable.party_popper, R.drawable.weight_lifting, R.drawable.flying_circus, R.drawable.londoner};

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
                    openDetailActivity(event.getCardName(), event.getImageResourceId(), position);
                }
            });
        }

        public void openDetailActivity(String title, Integer tag, int position) {
            Intent i = new Intent(c, DetailActivity.class);

            //PACK DATA
            i.putExtra("TITLE_KEY", title);
            i.putExtra("TAG_KEY", tag);
            i.putExtra("POSITION_KEY", position);

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
        listitems.clear();
        for (int i = 0; i < 6; i++) {
            Event item = new Event();
            item.setCardName(Events[i]);
            item.setImageResourceId(Images[i]);
            listitems.add(item);
        }
    }
}
