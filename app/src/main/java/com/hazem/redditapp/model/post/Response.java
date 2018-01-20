package com.hazem.redditapp.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hazem Ali.
 * On 1/19/2018.
 */

public class Response {

        @SerializedName("kind")
        @Expose
        public String kind;
        @SerializedName("data")
        @Expose
        public Data data;
}
