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

public class SavedRecyclerAdapter extends RecyclerView.Adapter<SavedRecyclerAdapter.ViewHolder> {

    private Cursor dataCursor;

    public SavedRecyclerAdapter(Cursor dataCursor) {
        this.dataCursor = dataCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_saved_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);
        int postTitleIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.POST_TITLE);
        int subredditIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.SUBREDDIT_NAME);
        int isPostIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.IS_POST);
        int user_commentIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.USER_COMMENT);

        String postTitle = dataCursor.getString(postTitleIndx);
        String subreddit = dataCursor.getString(subredditIndx);
        String isPost = dataCursor.getString(isPostIndx);
        String user_comment = dataCursor.getString(user_commentIndx);

        if (isPost != null && isPost.equals("0")) {
            holder.user_comment.setVisibility(View.VISIBLE);
            holder.user_comment.setText(String.valueOf(user_comment));
        }

        holder.post_title.setText(String.valueOf(postTitle));
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
