package com.hazem.redditapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.redditapp.R;
import com.hazem.redditapp.activities.PostDetailsActivity;
import com.hazem.redditapp.model.subreddit.Child;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.utils.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by Hazem Ali.
 * On 1/13/2018.
 */

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {

    private SubredditListing subredditListing;
    private Context context;

    public PostsRecyclerViewAdapter(Context context, SubredditListing subredditListing) {
        this.context = context;
        this.subredditListing = subredditListing;
    }

    @Override
    public PostsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subreddit_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PostsRecyclerViewAdapter.ViewHolder holder, int position) {
        Child child = subredditListing.data.children.get(position);
        holder.BindData(child);
    }

    @Override
    public int getItemCount() {
        return subredditListing.data.children.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subreddit_name, post_owner_user_name, post_title, vote_count, comment_count;
        ImageView post_poster;
        ImageButton up_vote, down_vote;
        LinearLayout comments_btn, share_btn;

        ViewHolder(View itemView) {
            super(itemView);

            subreddit_name = itemView.findViewById(R.id.subreddit_name);
            post_owner_user_name = itemView.findViewById(R.id.post_owner_user_name);
            post_title = itemView.findViewById(R.id.post_title);

            post_poster = itemView.findViewById(R.id.post_poster);
            up_vote = itemView.findViewById(R.id.up_vote);
            down_vote = itemView.findViewById(R.id.down_vote);
            vote_count = itemView.findViewById(R.id.vote_count);

            comment_count = itemView.findViewById(R.id.comment_count);
            comments_btn = itemView.findViewById(R.id.comments_btn);

            share_btn = itemView.findViewById(R.id.share_btn);
        }

        void BindData(final Child child) {
            subreddit_name.setText(String.valueOf(child.data.subredditNamePrefixed));
            post_owner_user_name.setText(String.valueOf(child.data.author));
            post_title.setText(String.valueOf(child.data.title));
            String imageUrl;

            try {
                imageUrl = child.data.preview.images.get(0).source.url;
            } catch (Exception e) {
                imageUrl = "";
            }
            if (!imageUrl.isEmpty())
                Picasso.with(context)
                        .load(imageUrl)
                        .resize(300, 140)
                        .placeholder(R.drawable.images)
                        .error(R.drawable.logo)
                        .into(post_poster);

            post_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(child.data.url));
                    context.startActivity(browserIntent);
                }
            });

            up_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "up", Toast.LENGTH_SHORT).show();
                }
            });
            down_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "down", Toast.LENGTH_SHORT).show();
                }
            });

            String c = String.valueOf(child.data.score);
            int count = child.data.score;
            if (count > 1000) {
                c = String.valueOf(count / 1000) + "K";
            }
            vote_count.setText(c);


            comment_count.setText(String.valueOf(child.data.numComments));
            comments_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PostDetailsActivity.class)
                            .putExtra(Constants.POST_ID, child.data.id)
                            .putExtra(Constants.POST_SUBREDDIT_NAME, child.data.subreddit));
                }
            });
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, child.data.title);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });
        }
    }

}
