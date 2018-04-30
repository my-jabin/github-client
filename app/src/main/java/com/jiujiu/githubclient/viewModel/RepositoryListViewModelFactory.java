package com.jiujiu.githubclient.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class RepositoryListViewModelFactory implements ViewModelProvider.Factory {

    private String userName;
    public RepositoryListViewModelFactory( String userName) {
        this.userName = userName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RepositoryListViewModel(userName);
    }
}
