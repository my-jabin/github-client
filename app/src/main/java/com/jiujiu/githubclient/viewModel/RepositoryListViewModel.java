package com.jiujiu.githubclient.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.jiujiu.githubclient.data.DataRepository;
import com.jiujiu.githubclient.data.local.RepositoryEntity;

import java.util.List;

import javax.inject.Inject;

public class RepositoryListViewModel extends ViewModel {
    private static final String TAG = "RepositoryListViewModel";

    public DataRepository mDataRepository;
    private LiveData<List<RepositoryEntity>> repositoriesInDb = new MutableLiveData<>();
    private MediatorLiveData<List<RepositoryEntity>> mRepositoryList = new MediatorLiveData<>();

    @Inject
    public RepositoryListViewModel(DataRepository dataRepository) {
        this.mDataRepository = dataRepository;
    }

    public LiveData<List<RepositoryEntity>> getRepositories() {
        return mRepositoryList;
    }

    public void startLoading(String ownerName) {
        Log.d(TAG, "startLoading: start");
        mRepositoryList.removeSource(repositoriesInDb);
        repositoriesInDb = mDataRepository.getRepositoriesByOwner(ownerName);
        mRepositoryList.addSource(repositoriesInDb, repositoryEntities -> mRepositoryList.setValue(repositoryEntities));
        Log.d(TAG, "startLoading: finish. Now repositoryList has an observer to the local database");
    }

}
