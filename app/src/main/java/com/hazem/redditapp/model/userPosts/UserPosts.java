
package com.hazem.redditapp.model.userPosts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPosts {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("data")
    @Expose
    public Data data;

}
