package com.hazem.redditapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hazem.redditapp.TempActivity;


public class SessionManager {
    public static final String CODE = "code";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRES_HTOKEN = "refreshToken";

    // Sharedpref file name
    private static final String PREF_NAME = "LoggingSession";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FIRST_TIME = "is_first_time";
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context _context;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        editor = pref.edit();
    }


    /**
     * Create login session
     */
    public void createLoginSession(String code, String accessToken, String refreshToken) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(CODE, code);
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(REFRES_HTOKEN, refreshToken);

        // commit changes
        editor.commit();
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, TempActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isFirstTime() {
        return pref.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean firstTime) {
        editor.putBoolean(IS_FIRST_TIME, firstTime);
        editor.apply();
    }

}
