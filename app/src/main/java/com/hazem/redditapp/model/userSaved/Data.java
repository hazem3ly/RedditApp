
package com.hazem.redditapp.model.userSaved;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("after")
    @Expose
    public Object after;
    @SerializedName("dist")
    @Expose
    public Integer dist;
    @SerializedName("modhash")
    @Expose
    public String modhash;
    @SerializedName("whitelist_status")
    @Expose
    public Object whitelistStatus;
    @SerializedName("children")
    @Expose
    public List<Child> children = null;
    @SerializedName("before")
    @Expose
    public Object before;

}
