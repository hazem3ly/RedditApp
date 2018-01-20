package com.hazem.redditapp;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.hazem.redditapp.activities.MainActivity;
import com.hazem.redditapp.model.RefreshToken;
import com.hazem.redditapp.model.post.PostListing;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.network.RestClient;
import com.hazem.redditapp.utils.Constants;
import com.hazem.redditapp.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hazem Ali.
 * On 1/19/2018.
 */

public class RedditApi {

    public static void getSubredditList(String filter, OnDataReady onDataReadyListener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            getDataPublic(filter, onDataReadyListener);
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndGetData(sessionManager, filter, onDataReadyListener);
        } else {
            getDataAuth(sessionManager.getAuthorizationKey(), filter, onDataReadyListener);
        }


    }

    private static void updateSessionAndGetData(final SessionManager sessionManager, final String filter,
                                                final OnDataReady onDataReadyListener) {
        RestClient client = new RestClient();

        String basicAuth =
                "Basic " + Base64.encodeToString((Constants.CLIENT_ID + ":").getBytes(), Base64.NO_WRAP);

        Call<RefreshToken> accessTokenCall = client.getApi_service().refreshToken(basicAuth,
                "refresh_token", sessionManager.getRefreshToken());
        accessTokenCall.enqueue(new Callback<RefreshToken>() {
            @Override
            public void onResponse(@NonNull Call<RefreshToken> call, @NonNull Response<RefreshToken> response) {
                if (response.isSuccessful()) {
                    RefreshToken refreshToken = response.body();
                    if (refreshToken != null) {
                        sessionManager.updateAccessToken(refreshToken.accessToken);
                        getSubredditList(filter, onDataReadyListener);
                    } else {
                        if (onDataReadyListener != null) onDataReadyListener.OnFailure();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
                if (onDataReadyListener != null) onDataReadyListener.OnFailure();
            }
        });


    }

    private static void getDataAuth(String auth, String filter, final OnDataReady onDataReadyListener) {
        RestClient client = new RestClient();
        Call<SubredditListing> call = client.getApi_service().loadHomeSubreddits(auth,
                MainActivity.type + filter, new HashMap<String, String>());
        call.enqueue(new Callback<SubredditListing>() {
            @Override
            public void onResponse(@NonNull Call<SubredditListing> call, @NonNull Response<SubredditListing> response) {
                if (response.isSuccessful()) {
                    SubredditListing subredditListing = response.body();
                    if (subredditListing != null) {
                        if (onDataReadyListener != null)
                            onDataReadyListener.OnResponseSuccessfully(subredditListing);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubredditListing> call, @NonNull Throwable t) {
                if (onDataReadyListener != null)
                    onDataReadyListener.OnFailure();
            }
        });
    }

    private static void getDataPublic(String filter, final OnDataReady onDataReadyListener) {

        RestClient client = new RestClient();
        Call<SubredditListing> call = client.getApi_service().loadSubreddits(
                MainActivity.type + filter, new HashMap<String, String>());
        call.enqueue(new Callback<SubredditListing>() {
            @Override
            public void onResponse(@NonNull Call<SubredditListing> call, @NonNull Response<SubredditListing> response) {
                if (response.isSuccessful()) {
                    SubredditListing subredditListing = response.body();
                    if (subredditListing != null) {
                        if (onDataReadyListener != null)
                            onDataReadyListener.OnResponseSuccessfully(subredditListing);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubredditListing> call, @NonNull Throwable t) {
                if (onDataReadyListener != null)
                    onDataReadyListener.OnFailure();
            }
        });
    }

    public static void loadPostDetails(String postId,String subredditName, final OnDataReady onDataReadyListener) {
        RestClient client = new RestClient();

        Call<List<PostListing>> call = client.getApi_service().loadPostDetails(subredditName,postId);
        call.enqueue(new Callback<List<PostListing>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostListing>> call, @NonNull Response<List<PostListing>> response) {
                if (response.isSuccessful()){
                    List<PostListing> postListing = response.body();
                    if (postListing !=null){
                        if (onDataReadyListener != null)
                            onDataReadyListener.OnResponseSuccessfully(postListing);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostListing>> call, @NonNull Throwable t) {
                if (onDataReadyListener != null)
                    onDataReadyListener.OnFailure();
            }
        });
    }

    public interface OnDataReady {
        void OnResponseSuccessfully(Object result);

        void OnFailure();
    }

}
