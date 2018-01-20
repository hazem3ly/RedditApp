package com.hazem.redditapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.redditapp.R;
import com.hazem.redditapp.RedditApi;
import com.hazem.redditapp.adapters.CommentsRecyclerViewAdapter;
import com.hazem.redditapp.model.post.PostListing;
import com.hazem.redditapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    ImageButton open_in_new, up_vote, down_vote;
    ImageView post_poster;
    TextView vote_count, comment_count, post_title, error_load_text;
    LinearLayout container, share_btn;
    RecyclerView comments_list;
    EditText add_comment_et;
    ProgressBar progressBar;
    ActionBar actionBar;
    CommentsRecyclerViewAdapter adapter;

    ScrollView scrollView;
    LinearLayout comment_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        if (!getIntent().hasExtra(Constants.POST_ID)) finish();

        initialViews();

        getData();

        initialToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    private void getData() {
        String postId = getIntent().getStringExtra(Constants.POST_ID);
        String subredditName = getIntent().getStringExtra(Constants.POST_SUBREDDIT_NAME);
        showProgress(true, false);
        RedditApi.loadPostDetails(postId, subredditName, new RedditApi.OnDataReady() {
            @Override
            public void OnResponseSuccessfully(Object postListing) {
                List<PostListing> postListing1 = (List<PostListing>) postListing;
                if (postListing1 != null && postListing1.size() > 1) {
                    showProgress(false, true);

                    updateViews(postListing1);
                } else showProgress(false, false);

            }

            @Override
            public void OnFailure() {
                showProgress(false, false);
            }
        });
    }

    private void showProgress(boolean isProgress, boolean loadedSuccessfully) {

        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        error_load_text.setVisibility((!isProgress && !loadedSuccessfully) ? View.VISIBLE : View.GONE);

    }

    private void updateViews(List<PostListing> postListing1) {
        updatePostData(postListing1.get(0));
        updateComments(postListing1.get(1));
    }

    private void updateComments(PostListing commentsData) {
        adapter = new CommentsRecyclerViewAdapter(this, commentsData.data.children);
        if (comments_list != null) {
            comments_list.setAdapter(adapter);
        }
    }

    private void updatePostData(final PostListing postData) {
        open_in_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(postData.data.children.get(0).data.url));
                startActivity(browserIntent);
            }
        });

        post_title.setText(String.valueOf(postData.data.children.get(0).data.title));
        if (actionBar != null) {
            actionBar.setTitle(String.valueOf(postData.data.children.get(0).data.title));
        }
        String imageUrl;

        try {
            imageUrl = postData.data.children.get(0).data.preview.images.get(0).source.url;
        } catch (Exception e) {
            imageUrl = "";
        }
        if (!imageUrl.isEmpty())
            Picasso.with(this)
                    .load(imageUrl)
                    .resize(350, 150)
                    .placeholder(R.drawable.images)
                    .error(R.drawable.logo)
                    .into(post_poster);
        up_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailsActivity.this, "up", Toast.LENGTH_SHORT).show();
            }
        });
        down_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailsActivity.this, "down", Toast.LENGTH_SHORT).show();
            }
        });

        String c = String.valueOf(postData.data.children.get(0).data.score);
        int count = postData.data.children.get(0).data.score;
        if (count > 1000) {
            c = String.valueOf(count / 1000) + "K";
        }
        vote_count.setText(c);

        comment_count.setText(String.valueOf(postData.data.children.get(0).data.numComments));

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, postData.data.children.get(0).data.title);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    private void initialViews() {

        scrollView = findViewById(R.id.scrollView);

        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progress_bar);
        error_load_text = findViewById(R.id.error_load_text);
        error_load_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        open_in_new = findViewById(R.id.open_in_new);
        up_vote = findViewById(R.id.up_vote);
        down_vote = findViewById(R.id.down_vote);

        post_poster = findViewById(R.id.post_poster);


        vote_count = findViewById(R.id.vote_count);
        comment_count = findViewById(R.id.comment_count);
        post_title = findViewById(R.id.post_title);

        share_btn = findViewById(R.id.share_btn);
        comment_btn = findViewById(R.id.comments_btn);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        add_comment_et = findViewById(R.id.add_comment_et);

        comments_list = findViewById(R.id.comments_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        comments_list.setLayoutManager(layoutManager);

    }
}
