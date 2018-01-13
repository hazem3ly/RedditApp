package com.hazem.redditapp.network;


import com.hazem.redditapp.model.AccessToken;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Hazem Ali.
 * On 12/25/2017.
 */

public interface Api_Service {

    @FormUrlEncoded
    @POST(Constants.ACCESS_TOKEN_URL)
    Call<AccessToken> retrieveAccessToken(@Header("Authorization") String authorization,
                                          @Field("grant_type") String grant_type,
                                          @Field("code") String code,
                                          @Field("redirect_uri") String redirectUti);

    @GET(Constants.BASE_URL + "{subbreddit}")
    Call<SubredditListing> loadSubreddits(@Path(value = "subbreddit") String subredditTyps);


}
