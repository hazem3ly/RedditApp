package com.hazem.redditapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hazem.redditapp.model.AccessToken;
import com.hazem.redditapp.network.RestClient;
import com.hazem.redditapp.utils.Constants;
import com.hazem.redditapp.utils.Navigator;
import com.hazem.redditapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    WebView webView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = App.getInstance().getCurrentSession();

        login();

    }


    private void login() {
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains(Constants.REDIRECT_URI) && !url.contains(Constants.CLIENT_ID)) {
                    Uri uri = Uri.parse(url);
                    if (uri.getQueryParameter("error") != null) {
                        startMainActivityAndFinish();
                    } else {
                        String state = uri.getQueryParameter("state");
                        if (state.equals(Constants.STATE)) {
                            webView.setVisibility(View.INVISIBLE);
                            String code = uri.getQueryParameter("code");
                            getAccessToken(code);
                        }
                    }
                }

            }
        });
        webView.loadUrl(Constants.LOGIN_URL);
    }

    private void getAccessToken(final String code) {

        RestClient client = new RestClient();
        String basicAuth = "Basic " + Base64.encodeToString((Constants.CLIENT_ID + ":").getBytes(), Base64.NO_WRAP);

        Call<AccessToken> call = client.getApi_service().
                retrieveAccessToken(basicAuth, "authorization_code", code, Constants.REDIRECT_URI);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    AccessToken accessToken = response.body();
                    if (accessToken != null)
                        sessionManager.createLoginSession(code, accessToken.accessToken, accessToken.refreshToken);
                }
                startMainActivityAndFinish();
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                startMainActivityAndFinish();
            }
        });
    }

    private void startMainActivityAndFinish(){
        Navigator.navigateToActivity(LoginActivity.this, MainActivity.class);
        LoginActivity.this.finish();

    }

}
