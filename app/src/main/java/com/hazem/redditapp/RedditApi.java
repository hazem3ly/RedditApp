package com.hazem.redditapp;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.hazem.redditapp.activities.MainActivity;
import com.hazem.redditapp.model.RefreshToken;
import com.hazem.redditapp.model.comment.CommentResult;
import com.hazem.redditapp.model.post.PostListing;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.model.user_details_mode.UserRequest;
import com.hazem.redditapp.network.RestClient;
import com.hazem.redditapp.network.UserDetailsTask;
import com.hazem.redditapp.utils.Constants;
import com.hazem.redditapp.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hazem Ali.
 * On 1/19/2018.
 */

public class RedditApi {

    public static int VOTE_UP = 1;
    public static int VOTE_DOWN = -1;
    public static int UN_VOTE = 0;

    // Retrieve Subreddits List
    /////////////////////////////////////////////////////////////////////////////////////////
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

    private static void updateSessionAndGetData(final SessionManager sessionManager,
                                                final String filter,
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

    private static void getDataAuth(String auth, String filter,
                                    final OnDataReady onDataReadyListener) {
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

    /////////////////////////////////////////////////////////////////////////////////////////

    // Vote On Thing (Post - Comment)
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void voteThing(int voteDir, String id, CallbackListener listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndVote(sessionManager, voteDir, id, listener);
        } else {
            votePostAuth(sessionManager.getAuthorizationKey(), voteDir, id, listener);
        }


    }
    private static void updateSessionAndVote(final SessionManager sessionManager,
                                             final int voteDir, final String id,
                                             final CallbackListener listener) {
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
                        votePostAuth(sessionManager.getAuthorizationKey(), voteDir, id, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    private static void votePostAuth(String auth, int voteDir, String id,
                                     final CallbackListener listener) {
        RestClient client = new RestClient();
        Call<ResponseBody> call = client.getApi_service().postVote(auth, String.valueOf(voteDir), id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) listener.OnResult(true);
                } else if (listener != null) listener.OnResult(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) listener.OnResult(false);
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    // Save Thing To User Account Saved List
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void saveUnSaveThing(String thingId, boolean save, CallbackListener listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndSave(sessionManager, thingId, save, listener);
        } else {
            saveThingAuth(sessionManager.getAuthorizationKey(), thingId, save, listener);

        }


    }
    private static void updateSessionAndSave(final SessionManager sessionManager,
                                             final String thingId, final boolean save,
                                             final CallbackListener listener) {
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
                        saveThingAuth(sessionManager.getAuthorizationKey(), thingId, save, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    private static void saveThingAuth(String auth, String thingId, boolean save,
                                      final CallbackListener listener) {
        RestClient client = new RestClient();
        if (save) {
            Call<ResponseBody> call = client.getApi_service().postSave(auth, String.valueOf(thingId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (listener != null) listener.OnResult(true);
                    } else if (listener != null) listener.OnResult(false);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (listener != null) listener.OnResult(false);
                }
            });
        } else {
            Call<ResponseBody> call = client.getApi_service().postUnsave(auth, String.valueOf(thingId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////


    public static void loadPostDetails(String postId, String subredditName,
                                       final OnDataReady onDataReadyListener) {
        RestClient client = new RestClient();

        Call<List<PostListing>> call = client.getApi_service().loadPostDetails(subredditName, postId);
        call.enqueue(new Callback<List<PostListing>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostListing>> call, @NonNull Response<List<PostListing>> response) {
                if (response.isSuccessful()) {
                    List<PostListing> postListing = response.body();
                    if (postListing != null) {
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
    // Add Comment TO Thing
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void commentToThing(String thingId, String commentBody,
                                      CallbackListener listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndComment(sessionManager, thingId, commentBody, listener);
        } else {
            commentThingAuth(sessionManager.getAuthorizationKey(), thingId, commentBody, listener);

        }

    }

    private static void commentThingAuth(String auth, String thingId,
                                         final String commentBody, final CallbackListener listener) {
        RestClient client = new RestClient();
        Call<CommentResult> call = client.getApi_service().postComment(auth, thingId, commentBody);
        call.enqueue(new Callback<CommentResult>() {
            @Override
            public void onResponse(Call<CommentResult> call, Response<CommentResult> response) {
                if (response.isSuccessful()) {
                    CommentResult commentResult = response.body();
                    if (commentResult != null) {
                        if (listener != null && commentResult.success) listener.OnResult(true);
                        else if (listener != null) listener.OnResult(false);
                    }
                } else if (listener != null) listener.OnResult(false);
            }

            @Override
            public void onFailure(Call<CommentResult> call, Throwable t) {
                if (listener != null) listener.OnResult(false);
            }
        });
    }

    private static void updateSessionAndComment(final SessionManager sessionManager,
                                                final String thingId, final String commentBody,
                                                final CallbackListener listener) {
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
                        commentThingAuth(sessionManager.getAuthorizationKey(), thingId, commentBody, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////


 // Add Comment TO Thing
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void loadUserData(OnLoadDataReady listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndLoadUserData(sessionManager, listener);
        } else {
            LoadUserDataAuth(sessionManager.getAuthorizationKey(), listener);

        }

    }

    private static void LoadUserDataAuth(String auth, final OnLoadDataReady listener) {
        UserDetailsTask userDetailsTask = new UserDetailsTask(new UserDetailsTask.OnRequestComplete() {
            @Override
            public void OnDataReady(UserRequest userRequest) {
                if (userRequest != null) {
                    if (listener!=null) listener.OnUserDataReady(userRequest);
                }else {
                    if (listener!=null) listener.OnUserDataReady(null);
                }
            }
        });
        userDetailsTask.execute(auth);
    }

    private static void updateSessionAndLoadUserData(final SessionManager sessionManager,
                                                final OnLoadDataReady listener) {
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
                        LoadUserDataAuth(sessionManager.getAuthorizationKey(), listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////



    public interface OnLoadDataReady{
        void OnUserDataReady(UserRequest userData);
    }
    public interface OnDataReady {
        void OnResponseSuccessfully(Object result);

        void OnFailure();
    }

    public interface CallbackListener {
        void OnResult(boolean success);
    }

}
