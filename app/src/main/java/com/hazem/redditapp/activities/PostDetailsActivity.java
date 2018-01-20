package com.hazem.redditapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hazem.redditapp.R;
import com.hazem.redditapp.RedditApi;
import com.hazem.redditapp.model.post.PostListing;
import com.hazem.redditapp.utils.Constants;

public class PostDetailsActivity extends AppCompatActivity {

    private String postUrl;
    private String postId,subredditName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        if (!getIntent().hasExtra(Constants.POST_ID)) finish();

        postId = getIntent().getStringExtra(Constants.POST_ID);
        subredditName = getIntent().getStringExtra(Constants.POST_SUBREDDIT_NAME);

        RedditApi.loadPostDetails(postId,subredditName, new RedditApi.OnDataReady() {
            @Override
            public void OnResponseSuccessfully(Object postListing) {
                PostListing postListing1 = (PostListing) postListing;
            }

            @Override
            public void OnFailure() {

            }
        });

    }
}
