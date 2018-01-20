package com.hazem.redditapp.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostListing {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("data")
    @Expose
    public Data data;


}
