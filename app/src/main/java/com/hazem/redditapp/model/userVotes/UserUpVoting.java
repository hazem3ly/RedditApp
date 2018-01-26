
package com.hazem.redditapp.model.userVotes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserUpVoting {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("data")
    @Expose
    public Data data;

}
