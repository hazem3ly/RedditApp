
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("modhash")
    @Expose
    public String modhash;
    @SerializedName("whitelist_status")
    @Expose
    public String whitelistStatus;
    @SerializedName("children")
    @Expose
    public List<Child> children = new ArrayList<>();
    @SerializedName("after")
    @Expose
    public String after;
    @SerializedName("before")
    @Expose
    public Object before;

}
