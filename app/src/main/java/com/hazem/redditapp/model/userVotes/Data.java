
package com.hazem.redditapp.model.userVotes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("after")
    @Expose
    public Object after;
    @SerializedName("dist")
    @Expose
    public Integer dist;
    @SerializedName("modhash")
    @Expose
    public Object modhash;

    @SerializedName("children")
    @Expose
    public List<Child> children = null;
    @SerializedName("before")
    @Expose
    public Object before;

}
