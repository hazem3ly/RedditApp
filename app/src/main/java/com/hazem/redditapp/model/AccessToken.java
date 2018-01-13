package com.hazem.redditapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hazem Ali.
 * On 12/25/2017.
 */

public class AccessToken {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("token_type")
    @Expose
    public String tokenType;
    @SerializedName("expires_in")
    @Expose
    public String expiresIn;
    @SerializedName("scope")
    @Expose
    public String scope;
    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;

}
