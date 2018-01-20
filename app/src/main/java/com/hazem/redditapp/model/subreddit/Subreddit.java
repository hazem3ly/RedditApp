
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subreddit {
//    transient

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

    @SerializedName("author")
    @Expose
    public String author;

    @SerializedName("saved")
    @Expose
    public Boolean saved;

    @SerializedName("score")
    @Expose
    public Integer score;

    @SerializedName("preview")
    @Expose
    public Preview preview;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("downs")
    @Expose
    public Integer downs;

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

    @SerializedName("ups")
    @Expose
    public Integer ups;
    @SerializedName("subreddit_name_prefixed")
    @Expose
    public String subredditNamePrefixed;
    @SerializedName("num_comments")
    @Expose
    public Integer numComments;
    @SerializedName("is_self")
    @Expose
    public Boolean isSelf;
    @SerializedName("visited")
    @Expose
    public Boolean visited;

    @SerializedName("is_video")
    @Expose
    public Boolean isVideo;


}
