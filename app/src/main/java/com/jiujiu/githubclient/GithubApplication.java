package com.jiujiu.githubclient;

import android.app.Application;

import com.jiujiu.githubclient.utils.InjectionUtil;

import java.util.concurrent.Executors;

public class GithubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        InjectionUtil.injectContext(this);

        Executors.newSingleThreadExecutor().execute(() -> {
            InjectionUtil.providesDataRepository();
        });
    }
}
