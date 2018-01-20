
package com.hazem.redditapp.model.post;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data__ {

    @SerializedName("modhash")
    @Expose
    public String modhash;
    @SerializedName("whitelist_status")
    @Expose
    public String whitelistStatus;
    @SerializedName("children")
    @Expose
    public List<Child_> children = null;
    @SerializedName("after")
    @Expose
    public Object after;
    @SerializedName("before")
    @Expose
    public Object before;

}
