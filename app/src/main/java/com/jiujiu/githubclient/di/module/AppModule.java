package com.jiujiu.githubclient.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.jiujiu.githubclient.data.local.LocalDatabase;
import com.jiujiu.githubclient.data.remote.GithubApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    GithubApi providesGithubApi() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi.class);
    }

    @Provides
    @Singleton
    LocalDatabase providesDatabase(Context context) {
        return Room.databaseBuilder(context, LocalDatabase.class, "githubclient.db").build();
    }

}
