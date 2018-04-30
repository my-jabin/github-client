package com.jiujiu.githubclient.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.jiujiu.githubclient.data.DataRepository;
import com.jiujiu.githubclient.data.local.RepositoryEntity;
import com.jiujiu.githubclient.utils.InjectionUtil;

import java.util.List;

public class RepositoryListViewModel extends ViewModel {

    private String mUserName;
    private DataRepository mDataRepository;
    private LiveData<List<RepositoryEntity>> repositoriesInDb;
    private MediatorLiveData<List<RepositoryEntity>> mRepositoryList = new MediatorLiveData<>();


    public RepositoryListViewModel(String userName) {
        this.mUserName = userName;
        mDataRepository = InjectionUtil.providesDataRepository();
        repositoriesInDb = mDataRepository.getRepositoriesByOwner(this.mUserName);
    }


    public LiveData<List<RepositoryEntity>> getRepositories() {
        return mRepositoryList;
    }


    public void startLoading() {
        mRepositoryList.removeSource(repositoriesInDb);
        repositoriesInDb = mDataRepository.getRepositoriesByOwner(mUserName);
        mRepositoryList.addSource(repositoriesInDb, repositoryEntities -> mRepositoryList.setValue(repositoryEntities));
    }

}
