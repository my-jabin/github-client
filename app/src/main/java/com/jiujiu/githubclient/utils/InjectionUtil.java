package com.jiujiu.githubclient.utils;

import android.content.Context;

import com.jiujiu.githubclient.data.DataRepository;
import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.LocalDatabase;
import com.jiujiu.githubclient.data.remote.GithubApi;
import com.jiujiu.githubclient.data.remote.RemoteDataSource;
import com.jiujiu.githubclient.data.remote.RetrofitClient;

import retrofit2.Retrofit;

public class InjectionUtil {

    private static Context mContext;

    public static void injectContext(Context context){
        mContext = context;
    }

    //todo: async injection by using dagger2
    public static DataRepository providesDataRepository(){
        return DataRepository.getInstance();
    }

    public static LocalDatabase providesDatabase(){
        return LocalDatabase.getInstance(mContext.getApplicationContext());
    }

   // public static AppExecutors providesExecutor(){
//        return AppExecutors.getInstance();
//    }

    public static LocalDataSource providesLocalDataSource(){
        return LocalDataSource.getInstance();
    }

    public static RemoteDataSource providesRemoteDataSource() {
        return RemoteDataSource.getInstance();
    }

    public static Retrofit providesRetrofit(){
        return RetrofitClient.getInstance();
    }

    public static GithubApi providesGithubApi(){
        return providesRetrofit().create(GithubApi.class);
    }

    public static Context prividesContext() {
        return mContext;
    }
}
