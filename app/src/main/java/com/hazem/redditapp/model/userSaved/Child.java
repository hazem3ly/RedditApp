
package com.hazem.redditapp.model.userSaved;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("data")
    @Expose
    public Saved data;

}
