
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gif {

    @SerializedName("source")
    @Expose
    public Source_ source;
    @SerializedName("resolutions")
    @Expose
    public List<Resolution_> resolutions = null;

}
