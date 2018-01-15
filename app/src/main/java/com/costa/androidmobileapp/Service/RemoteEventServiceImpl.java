package com.costa.androidmobileapp.Service;

import com.costa.androidmobileapp.BuildConfig;
import com.costa.androidmobileapp.Model.Event;
import com.costa.androidmobileapp.BuildConfig;
import com.costa.androidmobileapp.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RemoteEventServiceImpl {


    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.REMOTE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static RemoteEventServiceInterface service = null;

    public static RemoteEventServiceInterface getInstance(){
        if(service ==null){
            service = retrofit.create(RemoteEventServiceInterface.class);
        }
        return service;
    }

    private static List<Observer> observers = new ArrayList<Observer>();
    public static void attach(Observer observer){
        observers.add(observer);
    }

    public static void notifyAllObservers(){
        for(Observer observer:observers){
            observer.update();
        }
    }

    public interface RemoteEventServiceInterface{
        @PUT("/event/{name}.json")
        Call<Event> createEvent(@Path("name") String title,
                                 @Body Event review);

        @GET("/event/{name}.json")
        Call<Event> getEvent(@Path("name") String title);

        @GET("/event/.json")
        Call<Map<String,Event>> getAllEvents();

        @DELETE("/event/{name}.json")
        Call<Event> deleteEvent(@Path("name") String title);
    }

}
