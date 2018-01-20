package com.hazem.redditapp.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDetailsAndComments {


    @SerializedName("subreddit_id")
    @Expose
    public String subredditId;

    @SerializedName("subreddit")
    @Expose
    public String subreddit;

    @SerializedName("likes")
    @Expose
    public Object likes;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("view_count")
    @Expose
    public Object viewCount;
    @SerializedName("archived")
    @Expose
    public Boolean archived;
    @SerializedName("clicked")
    @Expose
    public Boolean clicked;

    @SerializedName("author")
    @Expose
    public String author;

    @SerializedName("saved")
    @Expose
    public Boolean saved;

    @SerializedName("score")
    @Expose
    public Integer score;

    @SerializedName("over_18")
    @Expose
    public Boolean over18;

    @SerializedName("preview")
    @Expose
    public Preview preview;
    @SerializedName("num_comments")
    @Expose
    public Integer numComments;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;
    @SerializedName("hide_score")
    @Expose
    public Boolean hideScore;

    @SerializedName("downs")
    @Expose
    public Integer downs;

    @SerializedName("thumbnail_height")
    @Expose
    public Integer thumbnailHeight;
    @SerializedName("parent_whitelist_status")
    @Expose
    public String parentWhitelistStatus;
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("subreddit_type")
    @Expose
    public String subredditType;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("created_utc")
    @Expose
    public Integer createdUtc;
    @SerializedName("subreddit_name_prefixed")
    @Expose
    public String subredditNamePrefixed;
    @SerializedName("distinguished")
    @Expose
    public String distinguished;
    @SerializedName("media")
    @Expose
    public Object media;
    @SerializedName("upvote_ratio")
    @Expose
    public Double upvoteRatio;

    @SerializedName("is_self")
    @Expose
    public Boolean isSelf;
    @SerializedName("visited")
    @Expose
    public Boolean visited;
    @SerializedName("num_reports")
    @Expose
    public Object numReports;
    @SerializedName("is_video")
    @Expose
    public Boolean isVideo;
    @SerializedName("ups")
    @Expose
    public Integer ups;
    @SerializedName("link_id")
    @Expose
    public String linkId;
    @SerializedName("parent_id")
    @Expose
    public String parentId;
    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("collapsed")
    @Expose
    public Boolean collapsed;
    @SerializedName("is_submitter")
    @Expose
    public Boolean isSubmitter;
    @SerializedName("collapsed_reason")
    @Expose
    public Object collapsedReason;
    @SerializedName("body_html")
    @Expose
    public String bodyHtml;
    @SerializedName("score_hidden")
    @Expose
    public Boolean scoreHidden;
    @SerializedName("controversiality")
    @Expose
    public Integer controversiality;
    @SerializedName("depth")
    @Expose
    public Integer depth;

}
