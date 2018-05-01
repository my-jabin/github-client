package com.jiujiu.githubclient.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.util.Log;

import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.OwnerEntity;
import com.jiujiu.githubclient.data.local.RepositoryEntity;
import com.jiujiu.githubclient.data.remote.RemoteDataSource;
import com.jiujiu.githubclient.utils.AppUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataRepository {
    private static final String TAG = "DataRepository";
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private Context mContext;

    @Inject
    public DataRepository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource, Context context) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.mContext = context;
    }

    @SuppressLint("CheckResult")
    public LiveData<OwnerEntity> getOwner(String ownerName) {
        MediatorLiveData<OwnerEntity> result = new MediatorLiveData<>();
        LiveData<OwnerEntity> owner = localDataSource.getOwnerByLogin(ownerName);
        if (AppUtils.isConneted(mContext)) {
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
        if (AppUtils.isConneted(mContext)) {
            remoteDataSource.fetchRepository(ownerName)
                    .subscribe(() -> {
                                Log.d(TAG, "fetch repository finished. subscribe to complete ");
                                result.addSource(repositories, result::setValue);
                            },
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
