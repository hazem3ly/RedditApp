package com.hazem.redditapp;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.hazem.redditapp.utils.SessionManager;

/**
 * Created by Hazem Ali.
 * On 1/17/2018.
 */

public class App extends Application {

    static App instance;
    private SessionManager currentSession;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        currentSession = new SessionManager(this);
        sAnalytics = GoogleAnalytics.getInstance(this);

    }

    public SessionManager getCurrentSession() {
        if (currentSession == null) currentSession = new SessionManager(this);
        return currentSession;
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

}
