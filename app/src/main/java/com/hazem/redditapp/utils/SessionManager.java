package com.hazem.redditapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hazem.redditapp.activities.TempActivity;

import java.util.Calendar;


public class SessionManager {
    private static final String CODE = "code";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRES_HTOKEN = "refreshToken";
    private static final String LOGIN_TIME = "time_logged";

    private static final long ONE_HOUR_IN_MILLI = 3600000;
    // Sharedpref file name
    private static final String PREF_NAME = "LoggingSession";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FIRST_TIME = "is_first_time";
    public static final String USER_NOT_LOGIN = "user_not_logged";
    public static final String SESSION_TIME_END = "session_time_ended";
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        editor.putLong(LOGIN_TIME, calendar.getTimeInMillis());

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

    public String getAuthorizationKey() {
        Boolean authorized = isUserAuthorized();

        if (authorized == null) return USER_NOT_LOGIN;

        if (!authorized) return SESSION_TIME_END;

        return "bearer  " + pref.getString(ACCESS_TOKEN, SESSION_TIME_END);
    }

    private Boolean isUserAuthorized() {

        if (pref.getBoolean(IS_LOGIN, false)) {
            long time = pref.getLong(LOGIN_TIME, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar.after(Calendar.getInstance());
        }

        return null;
    }

    public String getRefreshToken(){
        return pref.getString(REFRES_HTOKEN,"");
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

    public void updateAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN,accessToken);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        editor.putLong(LOGIN_TIME, calendar.getTimeInMillis());
        editor.apply();
    }
}
