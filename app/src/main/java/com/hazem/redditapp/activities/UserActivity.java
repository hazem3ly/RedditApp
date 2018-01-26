package com.hazem.redditapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hazem.redditapp.R;
import com.hazem.redditapp.adapters.CommentsRecyclerAdapter;
import com.hazem.redditapp.adapters.PostsRecyclerAdapter;
import com.hazem.redditapp.adapters.SavedRecyclerAdapter;
import com.hazem.redditapp.data_base.RedditContract;
import com.hazem.redditapp.model.userComments.UserComments;
import com.hazem.redditapp.model.userPosts.UserPosts;
import com.hazem.redditapp.model.userSaved.Child;
import com.hazem.redditapp.model.userSaved.UserSaved;
import com.hazem.redditapp.model.user_details_mode.UserRequest;
import com.hazem.redditapp.network.RedditApi;
import com.hazem.redditapp.utils.App;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity implements RedditApi.OnLoadDataReady, LoaderManager.LoaderCallbacks<Cursor> {

//    public static final int LOADER_ID_PERSON_OBJECT_TYPE = 0;
//    public ArrayList dbDataList = new ArrayList();
//    LoaderManager.LoaderCallbacks mProgressCallbacks;
//    LoaderManager loaderManager;

    private static final int POSTS_TAB = 0;
    private static final int COMMENTS_TAB = 1;
    private static final int SAVED_TAB = 2;
    private static final String RECYCLER_POSITION = "recycler_position";
    private static final String TAB_POSITION = "tab_posotion";
    private static final String USER_NAME = "user_name";
    RecyclerView recycler_view;
    TabLayout user_tabs;
    TextView user_name;
    ImageView user_icon;
    Tracker mTracker;
    int currentTab = COMMENTS_TAB;
    ProgressBar progressBar;
    private String userName;
    private String userId;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initialAnalytics();
        initialAdds();

        initialToolbar();
        initialViews();
        user_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showProgress(true);
                currentTab = tab.getPosition();
                loadDataFromApi(currentTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(TAB_POSITION)) {
                currentTab = savedInstanceState.getInt(TAB_POSITION, 0);
                loadDataFromDb(currentTab);
                user_tabs.getTabAt(currentTab).select();
            }

            if (savedInstanceState.containsKey(USER_NAME)){
                userName = savedInstanceState.getString(USER_NAME);
                user_name.setText(userName);
            }


            if (savedInstanceState.containsKey(RECYCLER_POSITION))
                layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_POSITION));
        } else {
            RedditApi.loadUserData(this);
            showProgress(true);
            loadDataFromApi(currentTab);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAB_POSITION, currentTab);
        outState.putString(USER_NAME, userName);
        if (layoutManager != null)
            outState.putParcelable(RECYCLER_POSITION, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void showProgress(boolean isProgress) {
        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        recycler_view.setVisibility(isProgress ? View.GONE : View.VISIBLE);
    }

    private void loadDataFromApi(int currentTab) {
        switch (currentTab) {
            case COMMENTS_TAB:
                getCommentsData();
                break;
            case POSTS_TAB:
                getPostsData();
                break;
            case SAVED_TAB:
                getSavedData();
                break;
        }
    }

    private void getSavedData() {
        if (userName == null) {
            loadDataFromDb(currentTab);
            return;
        }
        RedditApi.loadUserSaved(userName, new RedditApi.OnUserSavedReady() {
            @Override
            public void OnSavedReady(UserSaved userSaved) {
                if (userSaved != null) {
                    saveUserSavedPostInDb(userSaved);
                } else {
                    loadDataFromDb(currentTab);
                }
            }
        });
    }

    private void getPostsData() {
        if (userName == null) {
            loadDataFromDb(currentTab);
            return;
        }
        RedditApi.loadUserPosts(userName, new RedditApi.OnUserPostsReady() {
            @Override
            public void OnPostsReady(UserPosts userPosts) {
                if (userPosts != null) {
                    saveUserPostsPostInDb(userPosts);
                } else {
                    loadDataFromDb(currentTab);
                }
            }
        });
    }

    private void getCommentsData() {
        if (userName == null) {
            loadDataFromDb(currentTab);
            return;
        }
        RedditApi.loadUserComments(userName, new RedditApi.OnUserCommentsReady() {
            @Override
            public void OnCommentsReady(UserComments userComments) {
                if (userComments != null) {
                    saveUserCommentsInDb(userComments);
                } else {
                    loadDataFromDb(currentTab);
                }
            }
        });
    }

    private void saveUserSavedPostInDb(UserSaved userSaved) {

        getContentResolver().delete(RedditContract.USER_SAVED_TABLE.CONTENT_URI, null, null);


        for (Child child : userSaved.data.children) {
            ContentValues cv = new ContentValues();
            cv.put(RedditContract.USER_SAVED_TABLE.POST_TITLE, child.kind.equals("t3") ? child.data.title : child.data.linkTitle);
            cv.put(RedditContract.USER_SAVED_TABLE.SUBREDDIT_NAME, child.data.subreddit);
            cv.put(RedditContract.USER_SAVED_TABLE.USER_COMMENT, child.data.body);
            cv.put(RedditContract.USER_SAVED_TABLE.IS_POST, child.kind.equals("t3") ? "1" : "0");
            cv.put(RedditContract.USER_SAVED_TABLE.LINK_PERMALINK, child.data.linkPermalink);

            getContentResolver().insert(RedditContract.USER_SAVED_TABLE.CONTENT_URI, cv);
        }
        loadDataFromDb(currentTab);
    }

    private void saveUserPostsPostInDb(UserPosts userPosts) {
        getContentResolver().delete(RedditContract.USER_POSTS_TABLE.CONTENT_URI, null, null);


        for (com.hazem.redditapp.model.userPosts.Child child : userPosts.data.children) {
            ContentValues cv = new ContentValues();

            cv.put(RedditContract.USER_POSTS_TABLE.COLUMN_ID, child.data.id);
            cv.put(RedditContract.USER_POSTS_TABLE.SUBREDDIT, child.data.subreddit);
            cv.put(RedditContract.USER_POSTS_TABLE.AUTHER_NAME, child.data.author);
            cv.put(RedditContract.USER_POSTS_TABLE.POST_TITLE, child.data.title);
            cv.put(RedditContract.USER_POSTS_TABLE.VOTE_COUNT, String.valueOf(child.data.score));
            cv.put(RedditContract.USER_POSTS_TABLE.LINK_PERMALINK, child.data.permalink);
            cv.put(RedditContract.USER_POSTS_TABLE.COMMENTS_COUNT, String.valueOf(child.data.numComments));

            getContentResolver().insert(RedditContract.USER_POSTS_TABLE.CONTENT_URI, cv);
        }
        loadDataFromDb(currentTab);
    }

    private void saveUserCommentsInDb(UserComments userComments) {
        getContentResolver().delete(RedditContract.USER_COMMENTS_TABLE.CONTENT_URI, null, null);


        for (com.hazem.redditapp.model.userComments.Child child : userComments.data.children) {
            ContentValues cv = new ContentValues();
            cv.put(RedditContract.USER_COMMENTS_TABLE.COLUMN_ID, child.data.id);
            cv.put(RedditContract.USER_COMMENTS_TABLE.SUBREDDIT, child.data.subreddit);
            cv.put(RedditContract.USER_COMMENTS_TABLE.BODY, child.data.body);
            cv.put(RedditContract.USER_COMMENTS_TABLE.LINK_TITLE, child.data.linkTitle);
            cv.put(RedditContract.USER_COMMENTS_TABLE.LINK_PERMALINK, child.data.linkPermalink);

            getContentResolver().insert(RedditContract.USER_COMMENTS_TABLE.CONTENT_URI, cv);
        }
        loadDataFromDb(currentTab);
    }

    private void loadDataFromDb(int currentTab) {
        getSupportLoaderManager().restartLoader(currentTab, null, this);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case COMMENTS_TAB:
                String[] comments_proj = {
                        RedditContract.USER_COMMENTS_TABLE.COLUMN_ID,
                        RedditContract.USER_COMMENTS_TABLE.COLUMN_ID,
                        RedditContract.USER_COMMENTS_TABLE.SUBREDDIT,
                        RedditContract.USER_COMMENTS_TABLE.BODY,
                        RedditContract.USER_COMMENTS_TABLE.LINK_TITLE,
                        RedditContract.USER_COMMENTS_TABLE.LINK_PERMALINK};
                // Create a new CursorLoader with the following query parameters.
                return new CursorLoader(this, RedditContract.USER_COMMENTS_TABLE.CONTENT_URI,
                        comments_proj, null, null, null);

            case POSTS_TAB:
                String[] posts_proj = {
                        RedditContract.USER_POSTS_TABLE.COLUMN_ID,
                        RedditContract.USER_POSTS_TABLE.SUBREDDIT,
                        RedditContract.USER_POSTS_TABLE.AUTHER_NAME,
                        RedditContract.USER_POSTS_TABLE.POST_TITLE,
                        RedditContract.USER_POSTS_TABLE.VOTE_COUNT,
                        RedditContract.USER_POSTS_TABLE.COMMENTS_COUNT,
                        RedditContract.USER_POSTS_TABLE.LINK_PERMALINK};
                return new CursorLoader(this, RedditContract.USER_POSTS_TABLE.CONTENT_URI,
                        posts_proj, null, null, null);

            case SAVED_TAB:
                String[] saved_proj = {
                        RedditContract.USER_SAVED_TABLE.POST_TITLE,
                        RedditContract.USER_SAVED_TABLE.SUBREDDIT_NAME,
                        RedditContract.USER_SAVED_TABLE.USER_COMMENT,
                        RedditContract.USER_SAVED_TABLE.IS_POST,
                        RedditContract.USER_SAVED_TABLE.LINK_PERMALINK};
                return new CursorLoader(this, RedditContract.USER_SAVED_TABLE.CONTENT_URI,
                        saved_proj, null, null, null);

            default:
                return null;
        }
    }


    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        updateRecylerView(loader.getId(), cursor);
    }

    private void updateRecylerView(int currentTab, Cursor cursor) {
        showProgress(false);
        switch (currentTab) {
            case COMMENTS_TAB:
                CommentsRecyclerAdapter adapter = new CommentsRecyclerAdapter(cursor);
                recycler_view.setAdapter(adapter);
                break;
            case POSTS_TAB:
                PostsRecyclerAdapter adapter1 = new PostsRecyclerAdapter(cursor);
                recycler_view.setAdapter(adapter1);
                break;
            case SAVED_TAB:
                SavedRecyclerAdapter adapter2 = new SavedRecyclerAdapter(cursor);
                recycler_view.setAdapter(adapter2);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        //no swaping needed for progress update
    }


    private void initialViews() {
        progressBar = findViewById(R.id.progress_bar);
        recycler_view = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
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
                removeDb();
                App.getInstance().getCurrentSession().logoutUser();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeDb() {
        if (userId == null) return;
        Uri uri = RedditContract.REDDIT_USER_TABLE.CONTENT_URI.buildUpon().appendPath(String.valueOf(userId)).build();
        getContentResolver().delete(uri, null, null);

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
        this.userId = userData.id;

        showProgress(true);
        loadDataFromApi(currentTab);

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
        cv.put(RedditContract.REDDIT_USER_TABLE.COLUMN_ID, userData.id);
        cv.put(RedditContract.REDDIT_USER_TABLE.COLUMN_NAME, userData.name);
        cv.put(RedditContract.REDDIT_USER_TABLE.ICON_IMG, userData.iconImg);
        Uri uri = getContentResolver().insert(RedditContract.REDDIT_USER_TABLE.CONTENT_URI, cv);
        if (uri != null) {
            Toast.makeText(this, getString(R.string.user_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromDb() {

        Cursor data = getContentResolver().query(RedditContract.REDDIT_USER_TABLE.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (data != null && data.getCount() > 0) {

            if (data.moveToNext()) {

                int idIndex = data.getColumnIndex(RedditContract.REDDIT_USER_TABLE.COLUMN_ID);
                int nameIndex = data.getColumnIndex(RedditContract.REDDIT_USER_TABLE.COLUMN_NAME);
                int iconIndex = data.getColumnIndex(RedditContract.REDDIT_USER_TABLE.ICON_IMG);

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
        String whereClause = RedditContract.REDDIT_USER_TABLE.COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userData.id)};

        Cursor data = getContentResolver().query(RedditContract.REDDIT_USER_TABLE.CONTENT_URI,
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
