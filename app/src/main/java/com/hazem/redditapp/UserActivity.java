package com.hazem.redditapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hazem.redditapp.model.user_details_mode.UserRequest;
import com.hazem.redditapp.network.UserDetailsTask;
import com.hazem.redditapp.utils.SessionManager;

public class UserActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        sessionManager = App.getInstance().getCurrentSession();

       /* RestClient client = new RestClient();
        Call<UserRequest> call = client.getApi_service().getUserDetails(sessionManager.getAuthorizationKey());
        call.enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                if (response.isSuccessful()){
                    UserRequest userRequest = response.body();
                    if (userRequest!=null){
                        Log.d("","");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable t) {
                Log.d("","");

            }
        });*/
        String authentication = sessionManager.getAuthorizationKey();
        if (SessionManager.USER_NOT_LOGIN.equals(authentication)) {
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            finish();
        } else if (SessionManager.SESSION_TIME_END.equals(authentication)) {
            updateLoginSession();
        } else {
            loadUserData(authentication);
        }


    }

    private void loadUserData(String authentication) {
        UserDetailsTask userDetailsTask = new UserDetailsTask(new UserDetailsTask.OnRequestComplete() {
            @Override
            public void OnDataReady(UserRequest userRequest) {
                if (userRequest != null) {
                    String name = userRequest.name;
                }
            }
        });
        userDetailsTask.execute(authentication);

    }

    private void updateLoginSession() {
        Toast.makeText(this, "Update Session", Toast.LENGTH_SHORT).show();
    }
}
