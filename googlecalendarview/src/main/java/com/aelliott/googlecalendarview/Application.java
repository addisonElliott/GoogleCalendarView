package com.aelliott.googlecalendarview;

import android.util.Log;

import net.danlew.android.joda.JodaTimeAndroid;

public class Application extends android.app.Application
{
    private static final String TAG = "GoogleCalendarView";

    @Override
    public void onCreate()
    {
        super.onCreate();

        // It's important to initialize the ResourceZoneInfoProvider; otherwise
        // joda-time-android will not work.
        JodaTimeAndroid.init(this);

        Log.v(TAG, "Initialized Joda Time");
    }
}