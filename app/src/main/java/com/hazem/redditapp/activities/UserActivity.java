package com.hazem.redditapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hazem.redditapp.App;
import com.hazem.redditapp.R;
import com.hazem.redditapp.RedditApi;
import com.hazem.redditapp.data_base.RedditContract;
import com.hazem.redditapp.model.user_details_mode.UserRequest;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity implements RedditApi.OnLoadDataReady {


    RecyclerView recycler_view;
    TabLayout user_tabs;
    TextView user_name;
    ImageView user_icon;
    Tracker mTracker;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initialAnalytics();
        initialAdds();

        initialToolbar();
        initialViews();
        RedditApi.loadUserData(this);


    }

    private void initialViews() {
        recycler_view = findViewById(R.id.recycler_view);
        user_tabs = findViewById(R.id.user_tabs);
        user_name = findViewById(R.id.user_name);
        user_icon = findViewById(R.id.user_icon);
    }

    private void initialToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.app_name));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.logout:
                App.getInstance().getCurrentSession().logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialAdds() {

        AdView mAdView = findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

    }

    private void initialAnalytics() {
        mTracker = App.getInstance().getDefaultTracker();
        mTracker.setScreenName("UserActivity --> OnCreate()");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void OnUserDataReady(UserRequest userData) {
        if (userData == null) loadFromDb();
        else {
            saveUserInDb(userData);
            updateUi(userData);
        }
    }


    private void updateUi(UserRequest userData) {
        this.userName = userData.name;
        Picasso.with(this)
                .load(userData.iconImg)
                .fit()
                .placeholder(R.drawable.images)
                .error(R.drawable.logo)
                .into(user_icon);

        user_name.setText(userData.name);


    }

    private void saveUserInDb(UserRequest userData) {
        if (checkUserSaved(userData)) return;
        ContentValues cv = new ContentValues();
        cv.put(RedditContract.REDDIT_ENTRY.COLUMN_ID, userData.id);
        cv.put(RedditContract.REDDIT_ENTRY.COLUMN_NAME, userData.name);
        cv.put(RedditContract.REDDIT_ENTRY.ICON_IMG, userData.iconImg);
        Uri uri = getContentResolver().insert(RedditContract.REDDIT_ENTRY.CONTENT_URI, cv);
        if (uri != null) {
            Toast.makeText(this, getString(R.string.user_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromDb() {

        Cursor data = getContentResolver().query(RedditContract.REDDIT_ENTRY.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (data != null && data.getCount() > 0) {

            if (data.moveToNext()) {

                int idIndex = data.getColumnIndex(RedditContract.REDDIT_ENTRY.COLUMN_ID);
                int nameIndex = data.getColumnIndex(RedditContract.REDDIT_ENTRY.COLUMN_NAME);
                int iconIndex = data.getColumnIndex(RedditContract.REDDIT_ENTRY.ICON_IMG);

                UserRequest userData = new UserRequest();
                userData.id = data.getString(idIndex);
                userData.name = data.getString(nameIndex);
                userData.iconImg = data.getString(iconIndex);
                updateUi(userData);
            }

            data.close();
        }
    }

    private boolean checkUserSaved(UserRequest userData) {
        String whereClause = RedditContract.REDDIT_ENTRY.COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userData.id)};

        Cursor data = getContentResolver().query(RedditContract.REDDIT_ENTRY.CONTENT_URI,
                null,
                whereClause,
                whereArgs,
                null);
        boolean f = false;
        if (data != null) {
            f = data.getCount() > 0;
            data.close();
        }
        return f;

    }
}
