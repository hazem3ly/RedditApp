package com.hazem.redditapp;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.hazem.redditapp.activities.MainActivity;
import com.hazem.redditapp.model.RefreshToken;
import com.hazem.redditapp.model.comment.CommentResult;
import com.hazem.redditapp.model.post.PostListing;
import com.hazem.redditapp.model.subreddit.SubredditListing;
import com.hazem.redditapp.model.userComments.UserComments;
import com.hazem.redditapp.model.userPosts.UserPosts;
import com.hazem.redditapp.model.userSaved.UserSaved;
import com.hazem.redditapp.model.userVotes.UserUpVoting;
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


    // LoadUser Data
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
                    if (listener != null) listener.OnUserDataReady(userRequest);
                } else {
                    if (listener != null) listener.OnUserDataReady(null);
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


    // Load user comments
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void loadUserComments(String userName, OnUserCommentsReady listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndLoadComments(sessionManager, userName, listener);
        } else {
            loadUserCommentsOauth(sessionManager.getAuthorizationKey(), userName, listener);

        }

    }

    private static void loadUserCommentsOauth(String auth, String userName,
                                              final OnUserCommentsReady listener) {
        RestClient client = new RestClient();
        Call<UserComments> call = client.getApi_service().loadUserComments(auth, userName);
        call.enqueue(new Callback<UserComments>() {
            @Override
            public void onResponse(Call<UserComments> call, Response<UserComments> response) {
                if (response.isSuccessful()) {
                    UserComments userComments = response.body();
                    if (userComments != null) {
                        if (listener != null) listener.OnCommentsReady(userComments);
                    }
                } else if (listener != null) listener.OnCommentsReady(null);
            }

            @Override
            public void onFailure(Call<UserComments> call, Throwable t) {
                if (listener != null) listener.OnCommentsReady(null);
            }
        });
    }

    private static void updateSessionAndLoadComments(final SessionManager sessionManager,
                                                     final String userName,
                                                     final OnUserCommentsReady listener) {
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
                        loadUserCommentsOauth(sessionManager.getAuthorizationKey(), userName, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    // Load user Posts
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void loadUserPosts(String userName, OnUserPostsReady listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndLoadPosts(sessionManager, userName, listener);
        } else {
            loadUserPostsOauth(sessionManager.getAuthorizationKey(), userName, listener);

        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////

    private static void loadUserPostsOauth(String auth, String userName,
                                           final OnUserPostsReady listener) {
        RestClient client = new RestClient();
        Call<UserPosts> call = client.getApi_service().loadUserPosts(auth, userName);
        call.enqueue(new Callback<UserPosts>() {
            @Override
            public void onResponse(Call<UserPosts> call, Response<UserPosts> response) {
                if (response.isSuccessful()) {
                    UserPosts userPosts = response.body();
                    if (userPosts != null) {
                        if (listener != null) listener.OnPostsReady(userPosts);
                    }
                } else if (listener != null) listener.OnPostsReady(null);
            }

            @Override
            public void onFailure(Call<UserPosts> call, Throwable t) {
                if (listener != null) listener.OnPostsReady(null);
            }
        });
    }

    private static void updateSessionAndLoadPosts(final SessionManager sessionManager,
                                                  final String userName,
                                                  final OnUserPostsReady listener) {
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
                        loadUserPostsOauth(sessionManager.getAuthorizationKey(), userName, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    // Load user Posts
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void loadUserSaved(String userName, OnUserSavedReady listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndLoadSaved(sessionManager, userName, listener);
        } else {
            loadUserSavedOauth(sessionManager.getAuthorizationKey(), userName, listener);

        }

    }

    private static void loadUserSavedOauth(String auth, String userName,
                                           final OnUserSavedReady listener) {
        RestClient client = new RestClient();
        Call<UserSaved> call = client.getApi_service().loadUserSaved(auth, userName);
        call.enqueue(new Callback<UserSaved>() {
            @Override
            public void onResponse(Call<UserSaved> call, Response<UserSaved> response) {
                if (response.isSuccessful()) {
                    UserSaved userSaved = response.body();
                    if (userSaved != null) {
                        if (listener != null) listener.OnSavedReady(userSaved);
                    }
                } else if (listener != null) listener.OnSavedReady(null);
            }

            @Override
            public void onFailure(Call<UserSaved> call, Throwable t) {
                if (listener != null) listener.OnSavedReady(null);
            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    private static void updateSessionAndLoadSaved(final SessionManager sessionManager,
                                                  final String userName,
                                                  final OnUserSavedReady listener) {
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
                        loadUserSavedOauth(sessionManager.getAuthorizationKey(), userName, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }

    // Load user Upvoted Posts
    /////////////////////////////////////////////////////////////////////////////////////////
    public static void loadUserUpVoted(String userName, OnUserUpvotedReady listener) {

        SessionManager sessionManager = App.getInstance().getCurrentSession();

        if (sessionManager.getAuthorizationKey().equals(SessionManager.USER_NOT_LOGIN)) {
            return;
        } else if (sessionManager.getAuthorizationKey().equals(SessionManager.SESSION_TIME_END)) {
            updateSessionAndLoadUpvoted(sessionManager, userName, listener);
        } else {
            loadUserUpvoteOauth(sessionManager.getAuthorizationKey(), userName, listener);

        }

    }

    private static void loadUserUpvoteOauth(String auth, String userName,
                                            final OnUserUpvotedReady listener) {
        RestClient client = new RestClient();
        Call<UserUpVoting> call = client.getApi_service().loadUserUpvoted(auth, userName);
        call.enqueue(new Callback<UserUpVoting>() {
            @Override
            public void onResponse(Call<UserUpVoting> call, Response<UserUpVoting> response) {
                if (response.isSuccessful()) {
                    UserUpVoting userUpVoting = response.body();
                    if (userUpVoting != null) {
                        if (listener != null) listener.OnUpVotingReady(userUpVoting);
                    }
                } else if (listener != null) listener.OnUpVotingReady(null);
            }

            @Override
            public void onFailure(Call<UserUpVoting> call, Throwable t) {
                if (listener != null) listener.OnUpVotingReady(null);
            }
        });
    }

    private static void updateSessionAndLoadUpvoted(final SessionManager sessionManager,
                                                    final String userName,
                                                    final OnUserUpvotedReady listener) {
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
                        loadUserUpvoteOauth(sessionManager.getAuthorizationKey(), userName, listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshToken> call, @NonNull Throwable t) {
            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    public interface OnUserCommentsReady {
        void OnCommentsReady(UserComments userComments);
    }

    public interface OnUserPostsReady {
        void OnPostsReady(UserPosts userPosts);
    }

    public interface OnUserSavedReady {
        void OnSavedReady(UserSaved userSaved);
    }

    public interface OnUserUpvotedReady {
        void OnUpVotingReady(UserUpVoting userUpVoting);
    }

    public interface OnLoadDataReady {
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
