
package com.hazem.redditapp.model.userComments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("subreddit_id")
    @Expose
    public String subredditId;

    @SerializedName("link_id")
    @Expose
    public String linkId;
    @SerializedName("link_author")
    @Expose
    public String linkAuthor;
    @SerializedName("likes")
    @Expose
    public Boolean likes;

    @SerializedName("saved")
    @Expose
    public Boolean saved;
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("num_comments")
    @Expose
    public Integer numComments;

    @SerializedName("parent_id")
    @Expose
    public String parentId;
    @SerializedName("score")
    @Expose
    public Integer score;

    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("link_title")
    @Expose
    public String linkTitle;

    @SerializedName("downs")
    @Expose
    public Integer downs;

    @SerializedName("subreddit")
    @Expose
    public String subreddit;
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("permalink")
    @Expose
    public String permalink;

    @SerializedName("link_permalink")
    @Expose
    public String linkPermalink;

    @SerializedName("author_flair_text")
    @Expose
    public Object authorFlairText;
    @SerializedName("link_url")
    @Expose
    public String linkUrl;

    @SerializedName("subreddit_name_prefixed")
    @Expose
    public String subredditNamePrefixed;

    @SerializedName("subreddit_type")
    @Expose
    public String subredditType;
    @SerializedName("ups")
    @Expose
    public Integer ups;


}
