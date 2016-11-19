package com.mo.ie.meetingorganizer.main;

import android.app.Application;
import android.util.Log;

import com.mo.ie.meetingorganizer.db.DBManager;

import net.danlew.android.joda.JodaTimeAndroid;




public class MeetingOrganizerApp extends Application
{

    public DBManager dbManager = new DBManager(this);

    @Override
    public void onCreate()
    {
        super.onCreate();
        dbManager.open();
        Log.v("coffeemate", "CoffeeMate App Started");
        JodaTimeAndroid.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbManager.close();
    }
}