
package com.hazem.redditapp.model.subreddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Preview {

    @SerializedName("images")
    @Expose
    public List<Image> images = null;
    @SerializedName("enabled")
    @Expose
    public Boolean enabled;

}
