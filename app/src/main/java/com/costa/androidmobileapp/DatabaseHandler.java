package com.costa.androidmobileapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;



/**
 * Created by Costa on 17/12/2017.
 */


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "EventsDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE events " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "eventName TEXT, " +
                "numberOfPeople INTEGER, " +
                "locationName TEXT, " +
                "orgName TEXT, " +
                "imageId INTEGER) ";

        String sql1 = "INSERT INTO events " +
                "(eventName, numberOfPeople, locationName, orgName, imageId) " +
                "VALUES " +
                "(Library, 10, Bibleoteca, Costa, 0)";

        db.execSQL(sql);
        db.execSQL(sql1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS events";
        db.execSQL(sql);

        //onCreate(db);
    }
}

