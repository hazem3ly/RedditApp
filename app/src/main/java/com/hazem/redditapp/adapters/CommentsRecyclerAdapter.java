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

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    private Cursor dataCursor;

    public CommentsRecyclerAdapter(Cursor dataCursor) {
        this.dataCursor = dataCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);

        int subredditIndex = dataCursor.getColumnIndex(RedditContract.USER_COMMENTS_TABLE.SUBREDDIT);
        int bodyIndex = dataCursor.getColumnIndex(RedditContract.USER_COMMENTS_TABLE.BODY);
        int titleIndex = dataCursor.getColumnIndex(RedditContract.USER_COMMENTS_TABLE.LINK_TITLE);

        String subreddit = dataCursor.getString(subredditIndex);
        String body = dataCursor.getString(bodyIndex);
        String title = dataCursor.getString(titleIndex);

        holder.post_title.setText(String.valueOf(title));
        holder.user_comment.setText(String.valueOf(body));
        holder.subreddit_name.setText(String.valueOf(subreddit));

    }

    @Override
    public int getItemCount() {
        return dataCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView post_title;
        TextView subreddit_name;
        TextView user_comment;

        ViewHolder(View itemView) {
            super(itemView);

            post_title = itemView.findViewById(R.id.post_title);
            subreddit_name = itemView.findViewById(R.id.subreddit_name);
            user_comment = itemView.findViewById(R.id.user_comment);

        }


    }
}
