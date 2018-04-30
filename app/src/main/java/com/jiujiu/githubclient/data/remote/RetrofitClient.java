package com.jiujiu.githubclient.data.remote;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static Retrofit mInstance;
    private static final String BASEURL = "https://api.github.com";

    private RetrofitClient(){

    }

    public static Retrofit getInstance(){
        Log.d(TAG, "getInstance: ");
        if(mInstance == null){
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }
}
