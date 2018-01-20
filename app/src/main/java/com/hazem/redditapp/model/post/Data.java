
package com.hazem.redditapp.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    public List<Child> children = null;
    @SerializedName("after")
    @Expose
    public Object after;
    @SerializedName("before")
    @Expose
    public Object before;

}
