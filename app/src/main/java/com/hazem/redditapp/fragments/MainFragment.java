package com.hazem.redditapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hazem.redditapp.MainActivity;
import com.hazem.redditapp.R;
import com.hazem.redditapp.adapters.RecyclerViewAdapter;
import com.hazem.redditapp.model.DataChanged;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements DataChanged {


    RecyclerView recycler_view;
    RecyclerViewAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.dataChangedListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler_view = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
    }

    public void getList(String loadTyps) {
        RestClient client = new RestClient();
        retrofit2.Call<SubredditListing> call = client.getApi_service().loadSubreddits(loadTyps);
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

            }
        });
    }

    private void updateViews(SubredditListing subredditListing) {
        Toast.makeText(getContext(), "Finished Loading", Toast.LENGTH_SHORT).show();
        adapter = new RecyclerViewAdapter(subredditListing);
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void OnDataChanged(String types) {
        Toast.makeText(getContext(), "Start Loading", Toast.LENGTH_SHORT).show();
        getList(types);
    }
}
