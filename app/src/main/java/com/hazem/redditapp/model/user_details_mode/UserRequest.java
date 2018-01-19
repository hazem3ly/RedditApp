
package com.hazem.redditapp.model.user_details_mode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRequest {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("over_18")
    @Expose
    public Boolean over18;
    @SerializedName("is_gold")
    @Expose
    public Boolean isGold;
    @SerializedName("is_mod")
    @Expose
    public Boolean isMod;
    @SerializedName("icon_img")
    @Expose
    public String iconImg;
    @SerializedName("oauth_client_id")
    @Expose
    public String oauthClientId;
    @SerializedName("inbox_count")
    @Expose
    public Integer inboxCount;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("created")
    @Expose
    public Integer created;
    @SerializedName("comment_karma")
    @Expose
    public Integer commentKarma;
    @SerializedName("has_subscribed")
    @Expose
    public Boolean hasSubscribed;

}
