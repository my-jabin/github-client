package com.jiujiu.githubclient.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.OwnerEntity;
import com.jiujiu.githubclient.data.local.RepositoryEntity;
import com.jiujiu.githubclient.data.remote.RemoteDataSource;
import com.jiujiu.githubclient.utils.AppUtils;
import com.jiujiu.githubclient.utils.InjectionUtil;

import java.util.List;

public class DataRepository {
    private static final String TAG = "DataRepository";
    private static DataRepository mInstance;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    private DataRepository() {
        localDataSource = InjectionUtil.providesLocalDataSource();
        remoteDataSource = InjectionUtil.providesRemoteDataSource();
    }

    public static DataRepository getInstance() {
        if (mInstance == null) {
            synchronized (DataRepository.class) {
                if (mInstance == null) {
                    mInstance = new DataRepository();
                }
            }
        }
        return mInstance;
    }

    @SuppressLint("CheckResult")
    public LiveData<OwnerEntity> getOwner(String ownerName) {
        MediatorLiveData<OwnerEntity> result = new MediatorLiveData<>();
        LiveData<OwnerEntity> owner = localDataSource.getOwnerByLogin(ownerName);
        if (AppUtils.isConneted()) {
            Log.d(TAG, " APP is connected");
            remoteDataSource.loadOwnerAndSave(ownerName)
                    .subscribe(() -> result.addSource(owner, result::setValue),
                            throwable -> result.addSource(owner, result::setValue));
        } else {
            result.addSource(owner, result::setValue);
        }
        return result;
    }

    @SuppressLint("CheckResult")
    public LiveData<List<RepositoryEntity>> getRepositoriesByOwner(String ownerName) {
        MediatorLiveData<List<RepositoryEntity>> result = new MediatorLiveData<>();
        LiveData<List<RepositoryEntity>> repositories = localDataSource.getRepositoryByOwner(ownerName);
        if (AppUtils.isConneted()) {
            remoteDataSource.fetchRepository(ownerName)
                    .subscribe(() -> result.addSource(repositories, result::setValue),
                            throwable -> result.addSource(repositories, result::setValue));
        }else{
            result.addSource(repositories, result::setValue);
        }
        return result;
    }

    public void deleteAllRecords() {
        localDataSource.deleteAllRecords();
    }
}
