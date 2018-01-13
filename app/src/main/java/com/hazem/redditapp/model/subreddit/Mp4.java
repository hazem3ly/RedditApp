
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mp4 {

    @SerializedName("source")
    @Expose
    public Source__ source;
    @SerializedName("resolutions")
    @Expose
    public List<Resolution__> resolutions = null;

}
