
package com.hazem.redditapp.model.userComments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserComments {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("data")
    @Expose
    public Data data;

}
