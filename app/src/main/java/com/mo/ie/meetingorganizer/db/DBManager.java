package com.mo.ie.meetingorganizer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mo.ie.meetingorganizer.models.Meeting;

import org.joda.time.MutableDateTime;

import java.util.HashMap;

import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_CONTACT_ID;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_END_TIME;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_ID;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_LOCATION;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_MEETING_ID;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_NOTE;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_NOTIFICATION;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_START_TIME;
import static com.mo.ie.meetingorganizer.db.DBDesigner.KEY_TITLE;
import static com.mo.ie.meetingorganizer.db.DBDesigner.TABLE_CONTACTS;
import static com.mo.ie.meetingorganizer.db.DBDesigner.TABLE_MEETINGS;


public class DBManager {

    private SQLiteDatabase db;
    private DBDesigner dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBDesigner(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();

        Log.v("'MMMM'", "DB CREATED");
    }

    public void close() {
        db.close();
    }

    private static DBManager ourInstance;


    public static synchronized DBManager getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new DBManager(context);

        return ourInstance;
    }

    //This loads all the meetings from the DB and returns them as a hashmap
    //Called when the app is opened

    public HashMap<Long, Meeting> loadMeetings() {
        HashMap<Long, Meeting> meetings = new HashMap<>();
        String selectQueryGroups = "SELECT  * FROM " + TABLE_MEETINGS;

        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery(selectQueryGroups, null);

        if (c.moveToFirst()) {
            do {
                Meeting meeting = new Meeting();
                meeting.id = c.getInt(c.getColumnIndex(KEY_ID));
                meeting.notification = c.getInt(c.getColumnIndex(KEY_NOTIFICATION));
                meeting.title = c.getString(c.getColumnIndex(KEY_TITLE));
                meeting.location = c.getString(c.getColumnIndex(KEY_LOCATION));
                meeting.note = c.getString(c.getColumnIndex(KEY_NOTE));
                meeting.startDateTime = new MutableDateTime(c.getLong(c.getColumnIndex(KEY_START_TIME)));
                meeting.endDateTime = new MutableDateTime(c.getLong(c.getColumnIndex(KEY_END_TIME)));

                String selectQueryContacts = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_MEETING_ID + " = " + meeting.id;

                Cursor cContact = db.rawQuery(selectQueryContacts, null);

                if (cContact.moveToFirst()) {
                    do {
                        String contactString = cContact.getString(cContact.getColumnIndex(KEY_CONTACT_ID));
                        meeting.contacts.add(contactString);
                    } while (cContact.moveToNext());
                }
                cContact.close();

                meetings.put(meeting.id, meeting);
            } while (c.moveToNext());
        }
        c.close();

        return meetings;
    }

    //Creates a meeting in the db
    //Inserts into the meetings table and contacts table
    public Long createMeeting(Meeting meeting) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, meeting.title);
        values.put(KEY_LOCATION, meeting.location);
        values.put(KEY_NOTE, meeting.note);
        values.put(KEY_NOTIFICATION, meeting.notification);
        values.put(KEY_START_TIME, meeting.startDateTime.toDate().getTime());
        values.put(KEY_END_TIME, meeting.endDateTime.toDate().getTime());

        Long meetingId =  db.insertWithOnConflict(TABLE_MEETINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        for (String user : meeting.contacts) {
            ContentValues contactValues = new ContentValues();
            contactValues.put(KEY_MEETING_ID, meetingId);
            contactValues.put(KEY_CONTACT_ID, user);
            db.insertWithOnConflict(TABLE_CONTACTS, null, contactValues, SQLiteDatabase.CONFLICT_IGNORE);
        }

        return meetingId;
    }

    //Updates a meeting in the db
    public void updateMeeting(Meeting meeting) {
        db = dbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(KEY_ID, meeting.id);
        values.put(KEY_TITLE, meeting.title);
        values.put(KEY_LOCATION, meeting.location);
        values.put(KEY_NOTE, meeting.note);
        values.put(KEY_NOTIFICATION, meeting.notification);
        values.put(KEY_START_TIME, meeting.startDateTime.toDate().getTime());
        values.put(KEY_END_TIME, meeting.endDateTime.toDate().getTime());

        Long meetingId = db.insertWithOnConflict(TABLE_MEETINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.delete(TABLE_CONTACTS, KEY_MEETING_ID + " = ?", new String[]{String.valueOf(meeting.id)}); //delete old contacts

        for (String user : meeting.contacts) {
            ContentValues contactValues = new ContentValues();
            contactValues.put(KEY_MEETING_ID, meetingId);
            contactValues.put(KEY_CONTACT_ID, user);
            db.insertWithOnConflict(TABLE_CONTACTS, null, contactValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public void deleteMeeting(Meeting meeting) {


        db.delete(TABLE_MEETINGS, KEY_ID + " = ?", new String[]{String.valueOf(meeting.id)});

        db.delete(TABLE_CONTACTS, KEY_MEETING_ID + " = ?", new String[]{String.valueOf(meeting.id)});
    }
}
