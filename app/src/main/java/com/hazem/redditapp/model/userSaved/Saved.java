
package com.hazem.redditapp.model.userSaved;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Saved {

    @SerializedName("subreddit_id")
    @Expose
    public String subredditId;
    @SerializedName("approved_at_utc")
    @Expose
    public Object approvedAtUtc;
    @SerializedName("edited")
    @Expose
    public Boolean edited;
    @SerializedName("mod_reason_by")
    @Expose
    public Object modReasonBy;
    @SerializedName("banned_by")
    @Expose
    public Object bannedBy;
    @SerializedName("removal_reason")
    @Expose
    public Object removalReason;
    @SerializedName("link_id")
    @Expose
    public String linkId;
    @SerializedName("link_author")
    @Expose
    public String linkAuthor;
    @SerializedName("likes")
    @Expose
    public Boolean likes;
    @SerializedName("replies")
    @Expose
    public String replies;
    @SerializedName("user_reports")
    @Expose
    public List<Object> userReports = null;
    @SerializedName("saved")
    @Expose
    public Boolean saved;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("banned_at_utc")
    @Expose
    public Object bannedAtUtc;
    @SerializedName("mod_reason_title")
    @Expose
    public Object modReasonTitle;
    @SerializedName("gilded")
    @Expose
    public Integer gilded;
    @SerializedName("archived")
    @Expose
    public Boolean archived;
    @SerializedName("report_reasons")
    @Expose
    public List<Object> reportReasons = null;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("num_comments")
    @Expose
    public Integer numComments;
    @SerializedName("can_mod_post")
    @Expose
    public Boolean canModPost;
    @SerializedName("parent_id")
    @Expose
    public String parentId;
    @SerializedName("score")
    @Expose
    public Integer score;
    @SerializedName("approved_by")
    @Expose
    public Object approvedBy;
    @SerializedName("over_18")
    @Expose
    public Boolean over18;
    @SerializedName("mod_note")
    @Expose
    public Object modNote;
    @SerializedName("collapsed")
    @Expose
    public Boolean collapsed;
    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("link_title")
    @Expose
    public String linkTitle;

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("author_flair_css_class")
    @Expose
    public Object authorFlairCssClass;
    @SerializedName("downs")
    @Expose
    public Integer downs;
    @SerializedName("is_submitter")
    @Expose
    public Boolean isSubmitter;
    @SerializedName("collapsed_reason")
    @Expose
    public Object collapsedReason;
    @SerializedName("body_html")
    @Expose
    public String bodyHtml;
    @SerializedName("distinguished")
    @Expose
    public Object distinguished;
    @SerializedName("quarantine")
    @Expose
    public Boolean quarantine;
    @SerializedName("can_gild")
    @Expose
    public Boolean canGild;
    @SerializedName("removed")
    @Expose
    public Boolean removed;
    @SerializedName("approved")
    @Expose
    public Boolean approved;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("score_hidden")
    @Expose
    public Boolean scoreHidden;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("num_reports")
    @Expose
    public Integer numReports;
    @SerializedName("link_permalink")
    @Expose
    public String linkPermalink;
    @SerializedName("stickied")
    @Expose
    public Boolean stickied;
    @SerializedName("created")
    @Expose
    public Integer created;
    @SerializedName("subreddit")
    @Expose
    public String subreddit;
    @SerializedName("author_flair_text")
    @Expose
    public Object authorFlairText;
    @SerializedName("link_url")
    @Expose
    public String linkUrl;
    @SerializedName("spam")
    @Expose
    public Boolean spam;
    @SerializedName("created_utc")
    @Expose
    public Integer createdUtc;
    @SerializedName("subreddit_name_prefixed")
    @Expose
    public String subredditNamePrefixed;
    @SerializedName("controversiality")
    @Expose
    public Integer controversiality;
    @SerializedName("ignore_reports")
    @Expose
    public Boolean ignoreReports;
    @SerializedName("mod_reports")
    @Expose
    public List<Object> modReports = null;
    @SerializedName("subreddit_type")
    @Expose
    public String subredditType;
    @SerializedName("ups")
    @Expose
    public Integer ups;

}
