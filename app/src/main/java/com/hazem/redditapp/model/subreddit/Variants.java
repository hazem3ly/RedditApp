
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Variants {

    @SerializedName("gif")
    @Expose
    public Gif gif;
    @SerializedName("mp4")
    @Expose
    public Mp4 mp4;

}