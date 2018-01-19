package com.hazem.redditapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.hazem.redditapp.model.user_details_mode.UserRequest;
import com.hazem.redditapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hazem Ali.
 * On 1/18/2018.
 */

public class UserDetailsTask extends AsyncTask<String, Void, UserRequest> {

    private OnRequestComplete completeListener;

    public UserDetailsTask(OnRequestComplete completeListener) {
        this.completeListener = completeListener;
    }

    private String getResponseFromJsonURL(String url, String authorization) {
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        String JsonString = null;
        try {

            URL ur = new URL(url);

            connection = (HttpURLConnection) ur
                    .openConnection();

            connection.setReadTimeout(60 * 1000);
            connection.setConnectTimeout(60 * 1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authorization);
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JsonString = buffer.toString();
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error", " closing stream", e);
                }
            }
        }
        return JsonString;
    }

    @Override
    protected UserRequest doInBackground(String... params) {

        if (params.length == 0) return null;

        String authorization = params[0];

        try {
            return parserUserData(getResponseFromJsonURL(Constants.BASE_URL_OAUTH +
                    "/api/v1/me", authorization));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private UserRequest parserUserData(String responseFromJsonURL) throws JSONException {
        UserRequest userRequest = null;
        if (responseFromJsonURL != null) {
            JSONObject jsonObject = new JSONObject(responseFromJsonURL);
            userRequest = new UserRequest();

            if (jsonObject.has("id")) {
                userRequest.id = jsonObject.getString("id");
            }
            if (jsonObject.has("over_18")) {
                userRequest.over18 = jsonObject.getBoolean("over_18");
            }
            if (jsonObject.has("is_gold")) {
                userRequest.isGold = jsonObject.getBoolean("is_gold");
            }
            if (jsonObject.has("is_mod")) {
                userRequest.isMod = jsonObject.getBoolean("is_mod");
            }

            if (jsonObject.has("icon_img")) {
                userRequest.iconImg = jsonObject.getString("icon_img");
            }
            if (jsonObject.has("oauth_client_id")) {
                userRequest.oauthClientId = jsonObject.getString("oauth_client_id");
            }

            if (jsonObject.has("inboxCount")) {
                userRequest.inboxCount = jsonObject.getInt("inboxCount");
            }
            if (jsonObject.has("name")) {
                userRequest.name = jsonObject.getString("name");
            }

            if (jsonObject.has("created")) {
                userRequest.created = jsonObject.getInt("created");
            }


            if (jsonObject.has("created")) {
                userRequest.created = jsonObject.getInt("created");
            }

            if (jsonObject.has("commentKarma")) {
                userRequest.commentKarma = jsonObject.getInt("commentKarma");
            }


            if (jsonObject.has("has_subscribed")) {
                userRequest.hasSubscribed = jsonObject.getBoolean("has_subscribed");
            }


        }
        return userRequest;
    }

    @Override
    protected void onPostExecute(UserRequest userRequest) {
        if (completeListener != null)
            completeListener.OnDataReady(userRequest);
    }

    public interface OnRequestComplete {
        void OnDataReady(UserRequest userRequest);
    }
}
