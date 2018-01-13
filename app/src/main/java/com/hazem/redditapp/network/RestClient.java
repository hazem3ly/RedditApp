package com.hazem.redditapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hazem Ali.
 * On 12/25/2017.
 */

public class RestClient {

    private Api_Service api_service;

    public RestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api_service = retrofit.create(Api_Service.class);
    }

    public Api_Service getApi_service() {

        return api_service;

    }

}
