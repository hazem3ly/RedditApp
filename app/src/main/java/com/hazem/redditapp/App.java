package com.hazem.redditapp;

import android.app.Application;

import com.hazem.redditapp.utils.SessionManager;

/**
 * Created by Hazem Ali.
 * On 1/17/2018.
 */

public class App extends Application {

    static App instance;
    private SessionManager currentSession;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        currentSession = new SessionManager(this);
    }

    public SessionManager getCurrentSession() {
        if (currentSession == null) currentSession = new SessionManager(this);
        return currentSession;
    }

}
