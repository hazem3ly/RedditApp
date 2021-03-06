package com.hazem.redditapp.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hazem.redditapp.R;
import com.hazem.redditapp.adapters.ViewPagerAdapter;
import com.hazem.redditapp.fragments.ContrSubFragment;
import com.hazem.redditapp.fragments.HotSubFragment;
import com.hazem.redditapp.fragments.NewSubFragment;
import com.hazem.redditapp.fragments.RisingSubFragment;
import com.hazem.redditapp.fragments.TopSubFragment;
import com.hazem.redditapp.model.DataChanged;
import com.hazem.redditapp.utils.App;
import com.hazem.redditapp.utils.Constants;
import com.hazem.redditapp.utils.Navigator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static DataChanged dataChangedListener;
    public static String type = Constants.HOME_SUBRIDDIT;
    ViewPager viewPager;
    private EditText edtSearch;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_main);

        mTracker = App.getInstance().getDefaultTracker();
        mTracker.setScreenName("Main Activity --> OnCreate()");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initialToolbar();

        initialViewPager();

        initialTabs();

        initialBottomNavigationView();

    }

    private void initialBottomNavigationView() {

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_subreddits:
                        // when click on home change type string to home
                        type = Constants.HOME_SUBRIDDIT;
                        if (dataChangedListener != null)
                            dataChangedListener.OnDataChanged();
                        return true;
                    case R.id.all_subreddits:
                        type = Constants.ALL_SUBRIDDIT;
                        if (dataChangedListener != null)
                            dataChangedListener.OnDataChanged();
                        return true;
                    case R.id.popular_subreddits:
                        type = Constants.POPULAR_SUBRIDDIT;
                        if (dataChangedListener != null)
                            dataChangedListener.OnDataChanged();
                        return true;

                }
                return false;
            }
        });


    }

    private void initialToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    private void initialViewPager() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HotSubFragment());
        fragments.add(new NewSubFragment());
        fragments.add(new RisingSubFragment());
        fragments.add(new ContrSubFragment());
        fragments.add(new TopSubFragment());


        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

    }

    private void initialTabs() {

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                if (App.getInstance().getCurrentSession().isLoggedIn()) {
                    Navigator.navigateToActivity(this, UserActivity.class);
                } else {
                    Toast.makeText(this, getString(R.string.user_not_logged), Toast.LENGTH_SHORT).show();
                    Navigator.navigateToActivity(this, LoginActivity.class);
                }
                break;
        }
        return true;
    }
}
