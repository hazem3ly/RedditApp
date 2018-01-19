package com.hazem.redditapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hazem.redditapp.MainActivity;
import com.hazem.redditapp.R;
import com.hazem.redditapp.adapters.RecyclerViewAdapter;
import com.hazem.redditapp.model.DataChanged;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.network.RestClient;
import com.hazem.redditapp.utils.Constants;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RisingSubFragment extends Fragment implements DataChanged {


    RecyclerView recycler_view;
    RecyclerViewAdapter adapter;
    String filter = Constants.RISING_POSTS;

    public RisingSubFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_risingsub, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler_view = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
        getList();
    }

    public void getList() {
        recycler_view.setVisibility(View.GONE);
        RestClient client = new RestClient();
        Call<SubredditListing> call = client.getApi_service().loadSubreddits(MainActivity.type + filter,new HashMap<String, String>());
        call.enqueue(new Callback<SubredditListing>() {
            @Override
            public void onResponse(Call<SubredditListing> call, Response<SubredditListing> response) {
                if (response.isSuccessful()) {
                    SubredditListing subredditListing = response.body();
                    if (subredditListing != null) updateViews(subredditListing);
                }
            }

            @Override
            public void onFailure(Call<SubredditListing> call, Throwable t) {
                Log.d("","");

            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) MainActivity.dataChangedListener = this;
        OnDataChanged();


    }

    private void updateViews(SubredditListing subredditListing) {
        adapter = new RecyclerViewAdapter(subredditListing);
        if (recycler_view != null) {
            recycler_view.setVisibility(View.VISIBLE);
            recycler_view.setAdapter(adapter);

        }
    }

    @Override
    public void OnDataChanged() {
        if (isVisible()) {
            getList();
        }
    }
}
