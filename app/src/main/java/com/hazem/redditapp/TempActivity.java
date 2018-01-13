package com.hazem.redditapp;

import android.app.Activity;

import com.hazem.redditapp.utils.Navigator;
import com.hazem.redditapp.utils.SessionManager;

/**
 * Created by Hazem Ali.
 * On 1/12/2018.
 */

public class TempActivity extends Activity {

    @Override
    protected void onStart() {
        super.onStart();

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.isFirstTime()) Navigator.navigateToActivity(this, StarterActivity.class);
        else Navigator.navigateToActivity(this, MainActivity.class);

        finish();
    }
}
