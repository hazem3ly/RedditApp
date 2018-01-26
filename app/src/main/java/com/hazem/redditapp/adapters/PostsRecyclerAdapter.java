package com.hazem.redditapp.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hazem.redditapp.R;
import com.hazem.redditapp.data_base.RedditContract;

/**
 * Created by Hazem Ali.
 * On 1/26/2018.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder> {

    private Cursor dataCursor;

    public PostsRecyclerAdapter(Cursor dataCursor) {
        this.dataCursor = dataCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);

        int postTitleIndx = dataCursor.getColumnIndex(RedditContract.USER_POSTS_TABLE.POST_TITLE);
        int subredditIndx = dataCursor.getColumnIndex(RedditContract.USER_POSTS_TABLE.SUBREDDIT);
        int autherIndex = dataCursor.getColumnIndex(RedditContract.USER_POSTS_TABLE.AUTHER_NAME);
        int voteCountIndex = dataCursor.getColumnIndex(RedditContract.USER_POSTS_TABLE.VOTE_COUNT);
        int numCommentsIndex = dataCursor.getColumnIndex(RedditContract.USER_POSTS_TABLE.COMMENTS_COUNT);

        String postTitle = dataCursor.getString(postTitleIndx);
        String subreddit = dataCursor.getString(subredditIndx);
        String auther = dataCursor.getString(autherIndex);
        String voteCount = dataCursor.getString(voteCountIndex);
        String nomComments = dataCursor.getString(numCommentsIndex);

        holder.comment_count.setText(String.valueOf(nomComments));
        holder.post_owner_user_name.setText(String.valueOf(auther));
        holder.vote_count.setText(String.valueOf(voteCount));
        holder.subreddit_name.setText(String.valueOf(subreddit));
        holder.post_title.setText(String.valueOf(postTitle));

    }

    @Override
    public int getItemCount() {
        return dataCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView post_owner_user_name;
        TextView post_title;
        TextView subreddit_name;
        TextView vote_count;
        TextView comment_count;

        ViewHolder(View itemView) {
            super(itemView);
            subreddit_name = itemView.findViewById(R.id.subreddit_name);
            post_owner_user_name = itemView.findViewById(R.id.post_owner_user_name);
            post_title = itemView.findViewById(R.id.post_title);
            vote_count = itemView.findViewById(R.id.vote_count);
            comment_count = itemView.findViewById(R.id.comment_count);


        }


    }
}
