package com.jiujiu.githubclient.di.component;

import android.app.Application;

import com.jiujiu.githubclient.GithubApplication;
import com.jiujiu.githubclient.di.module.ActivityBuilder;
import com.jiujiu.githubclient.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(GithubApplication application);
}
