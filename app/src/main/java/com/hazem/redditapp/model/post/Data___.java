
package com.hazem.redditapp.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data___ {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("parent_id")
    @Expose
    public String parentId;
    @SerializedName("depth")
    @Expose
    public Integer depth;
    @SerializedName("children")
    @Expose
    public List<String> children = null;

}
