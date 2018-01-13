package com.hazem.redditapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.redditapp.fragments.MainFragment;
import com.hazem.redditapp.model.DataChanged;
import com.hazem.redditapp.utils.Constants;
import com.hazem.redditapp.utils.Navigator;

public class MainActivity extends AppCompatActivity {


    public static DataChanged dataChangedListener;
    private ActionBar actionBar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private TabLayout tabs;
    private BottomNavigationView navigation;

    private String type = Constants.HOME_SUBRIDDIT;
    private String filter = Constants.HOT_POSTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }


        Navigator.replaceFragmentInView(getSupportFragmentManager(), new MainFragment(), R.id.mFrame);

        tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        filter = Constants.HOT_POSTS;
                        break;
                    case 1:
                        filter = Constants.NEW_POSTS;
                        break;
                    case 2:
                        filter = Constants.RISING_POSTS;
                        break;
                    case 3:
                        filter = Constants.CONTROVERSIAL_POSTS;
                        break;
                }

                if (dataChangedListener != null) dataChangedListener.OnDataChanged(type + filter);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_subreddits:
                        // when click on home change type string to home
                        type = Constants.HOME_SUBRIDDIT;
                        if (dataChangedListener != null)
                            dataChangedListener.OnDataChanged(type + filter);
                        return true;
                    case R.id.all_subreddits:
                        type = Constants.ALL_SUBRIDDIT;
                        if (dataChangedListener != null)
                            dataChangedListener.OnDataChanged(type + filter);
                        return true;
                    case R.id.popular_subreddits:
                        type = Constants.POPULAR_SUBRIDDIT;
                        if (dataChangedListener != null)
                            dataChangedListener.OnDataChanged(type + filter);
                        return true;

                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                handleMenuSearch();
                break;
            case R.id.profile:
                Toast.makeText(this, "Profile Selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_24dp));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String s = edtSeach.getText().toString();
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));

            isSearchOpened = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataChangedListener != null) dataChangedListener.OnDataChanged(type + filter);

    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }
}
