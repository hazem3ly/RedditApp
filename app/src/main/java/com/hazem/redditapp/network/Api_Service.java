package com.hazem.redditapp.network;


import com.hazem.redditapp.model.AccessToken;
import com.hazem.redditapp.model.RefreshToken;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.model.user_details_mode.UserRequest;
import com.hazem.redditapp.utils.Constants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

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
    @FormUrlEncoded
    @POST(Constants.ACCESS_TOKEN_URL)
    Call<RefreshToken> refreshToken(@Header("Authorization") String authorization,
                                    @Field("grant_type") String grant_type,
                                    @Field("refresh_token") String refresh_token);


    @GET(Constants.BASE_URL + "/{subreddit}")
    Call<SubredditListing> loadSubreddits(@Path(value = "subreddit") String subredditTypes,
                                          @QueryMap Map<String, String> options);

    @GET(Constants.BASE_URL_OAUTH + "/{sortBy}")
    Call<SubredditListing> loadHomeSubreddits(@Header("Authorization") String authorization,
                                             @Path(value = "sortBy") String sortBy,
                                             @QueryMap Map<String, String> options);


    @GET(Constants.BASE_URL_OAUTH + "/api/v1/me")
    Call<UserRequest> getUserDetails(@Header("Authorization") String authorization);


}
