package com.jiujiu.githubclient.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jiujiu.githubclient.di.ViewModelKey;
import com.jiujiu.githubclient.viewModel.GithubViewModelFactory;
import com.jiujiu.githubclient.viewModel.MainActivityViewModel;
import com.jiujiu.githubclient.viewModel.RepositoryListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindsMainActivityViewModel(MainActivityViewModel mainActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RepositoryListViewModel.class)
    abstract ViewModel bindsRepoListViewModel(RepositoryListViewModel repositoryListViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(GithubViewModelFactory factory);

}
