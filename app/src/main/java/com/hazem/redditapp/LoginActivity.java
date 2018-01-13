package com.hazem.redditapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_login);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        sessionManager = new SessionManager(LoginActivity.this);

        webView = findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    setTitle(R.string.app_name);
            }
        });
        login();
        webView.loadUrl(Constants.LOGIN_URL);

    }


    private void login() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains(Constants.REDIRECT_URI) && !url.contains(Constants.CLIENT_ID)) {
                    Uri uri = Uri.parse(url);
                    if (uri.getQueryParameter("error") != null) {
                        Navigator.navigateToActivity(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.finish();
                    } else {
                        String state = uri.getQueryParameter("state");
                        if (state.equals(Constants.STATE)) {
                            final String code = uri.getQueryParameter("code");
                            Toast.makeText(LoginActivity.this, code, Toast.LENGTH_SHORT).show();
                            RestClient client = new RestClient();
                            final String basicAuth = "Basic " + Base64.encodeToString(
                                    (Constants.CLIENT_ID + ":").getBytes(), Base64.NO_WRAP);

                            Call<AccessToken> call = client.getApi_service().retrieveAccessToken(basicAuth,
                                    "authorization_code", code, Constants.REDIRECT_URI);
                            call.enqueue(new Callback<AccessToken>() {
                                @Override
                                public void onResponse(@NonNull Call<AccessToken> call,
                                                       @NonNull Response<AccessToken> response) {
                                    if (response.isSuccessful()) {
                                        AccessToken accessToken = response.body();
                                        if (accessToken != null)
                                            sessionManager.createLoginSession(code,
                                                    accessToken.accessToken, accessToken.refreshToken);
                                        Navigator.navigateToActivity(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.finish();
                                    } else {
                                        Navigator.navigateToActivity(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.finish();

                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                                    Navigator.navigateToActivity(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.finish();
                                }
                            });
                        }
                    }
                } else {
//                    Navigator.navigateToActivity(LoginActivity.this, MainActivity.class);
//                    LoginActivity.this.finish();

                }

            }
        });
    }


}
