package com.mo.ie.meetingorganizer.db;


import android.content.Context;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class DBDesigner extends SQLiteOpenHelper {

    // Database
    private static final String DATABASE_NAME = "meetings.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_MEETINGS = "meetings";
    public static final String TABLE_CONTACTS = "contacts";

    // Key IDs
    public static final String KEY_ID = "id";
    public static final String KEY_MEETING_ID = "meeting_id";
    public static final String KEY_CONTACT_ID = "contact_id";
    public static final String KEY_START_TIME = "starttime";
    public static final String KEY_END_TIME = "endtime";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_NOTE = "note";
    public static final String KEY_NOTIFICATION = "notification";

    private static final String CREATE_TABLE_MEETINGS = "CREATE TABLE "
            + TABLE_MEETINGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_LOCATION + " TEXT,"
            + KEY_NOTE + " TEXT,"
            + KEY_NOTIFICATION + " INTEGER,"
            + KEY_START_TIME + " DATETIME,"
            + KEY_END_TIME + " DATETIME" + ")";

    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE "
            + TABLE_CONTACTS + "(" + KEY_MEETING_ID + " INTEGER,"
            + KEY_CONTACT_ID + " TEXT" + ")";

    private static DBDesigner ourInstance;

    public DBDesigner(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static synchronized DBDesigner getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new DBDesigner(context);

        return ourInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEETINGS);
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}