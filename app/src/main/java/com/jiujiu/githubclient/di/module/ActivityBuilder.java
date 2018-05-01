package com.jiujiu.githubclient.di.module;

import com.jiujiu.githubclient.ui.MainActivity;
import com.jiujiu.githubclient.ui.RepositoryListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract MainActivity constributesMainActivity();

    @ContributesAndroidInjector
    abstract RepositoryListActivity constributesRepolistActivity();
}
