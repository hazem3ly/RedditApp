package com.hazem.redditapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hazem.redditapp.utils.App;
import com.hazem.redditapp.R;
import com.hazem.redditapp.network.RedditApi;
import com.hazem.redditapp.activities.MainActivity;
import com.hazem.redditapp.adapters.PostsRecyclerViewAdapter;
import com.hazem.redditapp.model.DataChanged;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.utils.Constants;
import com.hazem.redditapp.utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RisingSubFragment extends Fragment implements DataChanged {


    private static final String RECYCLER_POSITION = "recycler_position";
    RecyclerView recycler_view;
    PostsRecyclerViewAdapter adapter;
    String filter = Constants.RISING_POSTS;

    SwipeRefreshLayout swipe_refresh;
    ProgressBar progressBar;
    TextView error_load_text;

    SessionManager sessionManager;
    private LinearLayoutManager layoutManager;
    private boolean restoreSave;


    public RisingSubFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        sessionManager = App.getInstance().getCurrentSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subreddits, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialViews(view);
        if (savedInstanceState != null) {
            restoreSave = true;
            if (App.getInstance().getCurrentList() != null) {
                updateViews(App.getInstance().getCurrentList());
            }
            if (savedInstanceState.containsKey(RECYCLER_POSITION))
                layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_POSITION));
        } else {
            restoreSave = false;
            getList();
        }
    }

    private void initialViews(View view) {

        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getList();
                    }
                }
        );
        progressBar = view.findViewById(R.id.progress_bar);
        error_load_text = view.findViewById(R.id.error_load_text);
        error_load_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
        recycler_view = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (layoutManager != null)
            outState.putParcelable(RECYCLER_POSITION, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    public void getList() {
        showProgress(true, false);

        RedditApi.getSubredditList(filter, new RedditApi.OnDataReady() {
            @Override
            public void OnResponseSuccessfully(Object subredditListing) {
                updateViews((SubredditListing) subredditListing);
            }

            @Override
            public void OnFailure() {
                showProgress(false, false);
            }
        });

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) MainActivity.dataChangedListener = this;
        OnDataChanged();


    }
    private void showProgress(boolean isProgress, boolean loadedSuccessfully) {

        if (swipe_refresh.isRefreshing()&& !isProgress) {
            swipe_refresh.setRefreshing(false);
        }

        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);

        recycler_view.setVisibility((!isProgress && loadedSuccessfully) ? View.VISIBLE : View.GONE);

        error_load_text.setVisibility((!isProgress && !loadedSuccessfully) ? View.VISIBLE : View.GONE);

    }

    private void updateViews(SubredditListing subredditListing) {
        App.getInstance().setCurrentList(subredditListing);
        adapter = new PostsRecyclerViewAdapter(getContext(),subredditListing);
        if (recycler_view != null) {
            showProgress(false, true);
            recycler_view.setAdapter(adapter);

        }
    }

    @Override
    public void OnDataChanged() {
        if (isVisible() && !restoreSave) {
            getList();
        }
    }
}
