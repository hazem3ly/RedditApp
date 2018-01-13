package com.hazem.redditapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.hazem.redditapp.utils.Navigator;
import com.hazem.redditapp.utils.SessionManager;

public class StarterActivity extends AppCompatActivity {

    Button login,skip;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starter);

        sessionManager = new SessionManager(this);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setFirstTime(false);
                Navigator.navigateToActivity(StarterActivity.this, LoginActivity.class);
                finish();
            }
        });

        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setFirstTime(false);
                Navigator.navigateToActivity(StarterActivity.this, MainActivity.class);
                finish();
            }
        });

    }
}
