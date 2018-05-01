package com.jiujiu.githubclient.data.remote;

import android.util.Log;

import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.RepositoryEntity;
import com.jiujiu.githubclient.data.remote.model.RepositoryResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";
    private GithubApi githubApi;
    private LocalDataSource localDataSource;

    @Inject
    public RemoteDataSource(GithubApi githubApi, LocalDataSource localDataSource) {
        this.githubApi = githubApi;
        this.localDataSource = localDataSource;
    }

    public Completable loadOwnerAndSave(String owner) {
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
