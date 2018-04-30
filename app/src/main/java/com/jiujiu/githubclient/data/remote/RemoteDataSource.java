package com.jiujiu.githubclient.data.remote;

import android.util.Log;

import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.RepositoryEntity;
import com.jiujiu.githubclient.data.remote.model.RepositoryResponse;
import com.jiujiu.githubclient.utils.InjectionUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";

    private static RemoteDataSource mInstance;
    private GithubApi githubApi;
    private LocalDataSource localDataSource;

    private RemoteDataSource() {
        githubApi = InjectionUtil.providesGithubApi();
        localDataSource = InjectionUtil.providesLocalDataSource();
    }

    public static RemoteDataSource getInstance() {
        Log.d(TAG, "getInstance: ");
        if (mInstance == null) {
            synchronized (RemoteDataSource.class) {
                if (mInstance == null) {
                    mInstance = new RemoteDataSource();
                }
            }
        }
        return mInstance;
    }

    public Completable loadOwnerAndSave(String owner) {
        // rxjava2 way to fetch data
        Log.d(TAG, "load owner from remote server and save it");
        return githubApi.getOwner(owner)
                .subscribeOn(Schedulers.io())
                .map(ownerResponse -> ownerResponse == null ? null : ownerResponse.toOwnerEntity())
                .flatMapCompletable(ownerEntity -> Completable.fromRunnable(() -> localDataSource.refreshOwner(ownerEntity)));
    }

    public Completable fetchRepository(String owner) {
        Log.d(TAG, "start fetchRepository: ");

        return githubApi.getRepository(owner)
                .subscribeOn(Schedulers.io())
                .map(repositoryResponses -> {
                    List<RepositoryEntity> repositoryEntitylist = new ArrayList<>();
                    for (RepositoryResponse rr : repositoryResponses) {
                        repositoryEntitylist.add(rr.toRepositoryEntity());
                    }
                    return repositoryEntitylist;
                })
                .flatMapCompletable(repositoryEntityList -> Completable.fromAction(() -> {
                    if (repositoryEntityList != null && repositoryEntityList.size() > 0)
                        localDataSource.refreshRepository(repositoryEntityList, owner);
                }));

    }

}
