package com.nitheeshwar.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_START_DATE + " TEXT,"
                + KEY_END_DATE + " TEXT,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_START_DATE, event.getStartDate());
        values.put(KEY_END_DATE, event.getEndDate());
        values.put(KEY_LATITUDE, event.getLatitude());
        values.put(KEY_LONGITUDE, event.getLongitude());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setStartDate(cursor.getString(2));
                event.setEndDate(cursor.getString(3));
                event.setLatitude(cursor.getDouble(4));
                event.setLongitude(cursor.getDouble(5));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return eventList;
    }

    // Search for events by name
    public List<Event> searchEvents(String searchQuery) {
        List<Event> eventList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_NAME + " LIKE '%" + searchQuery + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));
                event.setStartDate(cursor.getString(2));
                event.setEndDate(cursor.getString(3));
                event.setLatitude(cursor.getDouble(4));
                event.setLongitude(cursor.getDouble(5));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return eventList;
    }
    public List<Event> searchEventsByNameOrLocation(String searchQuery) {
        List<Event> eventList = new ArrayList<>();
        // This query attempts to find matches in either the event's name or its location (latitude and longitude).
        // Note: This is a very basic form of location search and might not be optimal for all use cases.
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_NAME + " LIKE '%" + searchQuery +
                "%' OR CAST(" + KEY_LATITUDE + " AS TEXT) LIKE '%" + searchQuery +
                "%' OR CAST(" + KEY_LONGITUDE + " AS TEXT) LIKE '%" + searchQuery + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                event.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                event.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_START_DATE)));
                event.setEndDate(cursor.getString(cursor.getColumnIndex(KEY_END_DATE)));
                event.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)));
                event.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return eventList;
    }
}
