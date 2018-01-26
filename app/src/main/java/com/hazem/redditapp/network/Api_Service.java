package com.hazem.redditapp.network;


import com.hazem.redditapp.model.AccessToken;
import com.hazem.redditapp.model.RefreshToken;
import com.hazem.redditapp.model.comment.CommentResult;
import com.hazem.redditapp.model.post.PostListing;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.model.userComments.UserComments;
import com.hazem.redditapp.model.userPosts.UserPosts;
import com.hazem.redditapp.model.userSaved.UserSaved;
import com.hazem.redditapp.model.userVotes.UserUpVoting;
import com.hazem.redditapp.utils.Constants;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
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

//
//    @GET(Constants.BASE_URL_OAUTH + "/api/v1/me")
//    Call<UserRequest> getUserDetails(@Header("Authorization") String authorization);


    @GET(Constants.BASE_URL + "/r/{subbreddit_name}/comments/{id}/.json")
    Call<List<PostListing>> loadPostDetails(@Path(value = "subbreddit_name") String subreddit_name,
                                            @Path(value = "id") String postId);

    @FormUrlEncoded
    @POST(Constants.BASE_URL_OAUTH + "/api/vote")
    Call<ResponseBody> postVote(@Header("Authorization") String authorization,
                                @Field("dir") String dir,
                                @Field("id") String id);


    @FormUrlEncoded
    @POST(Constants.BASE_URL_OAUTH + "/api/save")
    Call<ResponseBody> postSave(@Header("Authorization") String authorization,
                                @Field("id") String id);


    @FormUrlEncoded
    @POST(Constants.BASE_URL_OAUTH + "/api/unsave")
    Call<ResponseBody> postUnsave(@Header("Authorization") String authorization,
                                  @Field("id") String id);


    @FormUrlEncoded
    @POST(Constants.BASE_URL_OAUTH + "/api/comment")
    Call<CommentResult> postComment(@Header("Authorization") String authorization,
                                    @Field("thing_id") String thingId,
                                    @Field("text") String commentBody);

    @GET(Constants.BASE_URL_OAUTH + "/user/{user_name}/comments/.json")
    Call<UserComments> loadUserComments(@Header("Authorization") String authorization,
                                        @Path(value = "user_name") String userName);

    @GET(Constants.BASE_URL_OAUTH + "/user/{user_name}/submitted/.json")
    Call<UserPosts> loadUserPosts(@Header("Authorization") String authorization,
                                  @Path(value = "user_name") String userName);

    @GET(Constants.BASE_URL_OAUTH + "/user/{user_name}/saved/.json")
    Call<UserSaved> loadUserSaved(@Header("Authorization") String authorization,
                                  @Path(value = "user_name") String userName);

    @GET(Constants.BASE_URL_OAUTH + "/user/{user_name}/upvoted/.json")
    Call<UserUpVoting> loadUserUpvoted(@Header("Authorization") String authorization,
                                       @Path(value = "user_name") String userName);



/*


    @GET(Constants.BASE_URL + "/r/{search}/search/.json")
    Call<> searchPosts(@Header("Authorization") String authorization,
                     @Path(value = "search") String search);
 */
}
