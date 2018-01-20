
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("source")
    @Expose
    public Source source;
    @SerializedName("id")
    @Expose
    public String id;

}
