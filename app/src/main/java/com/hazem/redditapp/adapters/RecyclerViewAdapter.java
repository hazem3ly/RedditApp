package com.hazem.redditapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hazem.redditapp.R;
import com.hazem.redditapp.model.subreddit.Child;
import com.hazem.redditapp.model.subreddit.SubredditListing;

/**
 * Created by Hazem Ali.
 * On 1/13/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    SubredditListing subredditListing;

    public RecyclerViewAdapter(SubredditListing subredditListing) {
        this.subredditListing = subredditListing;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subreddit_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Child child = subredditListing.data.children.get(position);
        String title = child.data.title;
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return subredditListing.data.children.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);

        }
    }

}
