package com.costa.androidmobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.costa.androidmobileapp.Model.Event;

import java.util.ArrayList;

/**
 * Created by Costa on 17/12/2017.
 */

public class TableControllerEvents extends DatabaseHandler {

    public TableControllerEvents(Context context) {
        super(context);
    }

    public boolean create(Event event) {

        /*eventName TEXT, " +
        "numberOfPeople INTEGER, " +
                "locationName TEXT, " +
                "orgName TEXT, " +
                "imageId INTEGER*/

        ContentValues values = new ContentValues();

        values.put("eventName", event.getCardName());
        values.put("numberOfPeople",  event.getNrOfPeople());
        values.put("locationName",  event.getLocation());
        values.put("orgName",  event.getNameOrganizor());
        values.put("imageId",  event.getImageResourceId());


        SQLiteDatabase db = this.getWritableDatabase();


        boolean createSuccessful = db.insert("events", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public ArrayList<Event> read() {

        ArrayList<Event> recordsList = new ArrayList<Event>();

        String sql = "SELECT * FROM events ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
                int nrOfPeople = Integer.parseInt(cursor.getString(cursor.getColumnIndex("numberOfPeople")));
                String locationName = cursor.getString(cursor.getColumnIndex("locationName"));
                String orgName = cursor.getString(cursor.getColumnIndex("orgName"));
                int imageId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("imageId")));

                Event objectEvent = new Event(id,eventName,nrOfPeople,locationName,orgName,imageId);

                recordsList.add(objectEvent);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public Event readSingleRecord(int eventId) {

        Event objectEvent = null;

        String sql = "SELECT * FROM events WHERE id = " + eventId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
            int nrOfPeople = Integer.parseInt(cursor.getString(cursor.getColumnIndex("numberOfPeople")));
            String locationName = cursor.getString(cursor.getColumnIndex("locationName"));
            String orgName = cursor.getString(cursor.getColumnIndex("orgName"));
            int imageId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("imageId")));

            objectEvent = new Event(id,eventName,nrOfPeople,locationName,orgName,imageId);


        }

        cursor.close();
        db.close();

        return objectEvent;

    }

    public boolean update(Event objectEvent) {

        ContentValues values = new ContentValues();

        values.put("eventName", objectEvent.getCardName());
        values.put("numberOfPeople",objectEvent.getNrOfPeople());
        values.put("locationName",objectEvent.getLocation());
        values.put("orgName", objectEvent.getNameOrganizor());
        values.put("imageId", objectEvent.getImageResourceId());


        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectEvent.getIdEvent()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("events", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("events", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
