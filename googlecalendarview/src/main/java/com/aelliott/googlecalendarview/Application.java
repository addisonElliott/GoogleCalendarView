package com.aelliott.googlecalendarview;

import android.util.Log;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class Application extends android.app.Application
{
    private static final String TAG = "GoogleCalendarView";

    @Override
    public void onCreate()
    {
        super.onCreate();

        // It's important to initialize the ResourceZoneInfoProvider; otherwise
        // AndroidThreeTen will not work.
        AndroidThreeTen.init(this);

        Log.v(TAG, "Initialized ThreeTenABP");
    }
}