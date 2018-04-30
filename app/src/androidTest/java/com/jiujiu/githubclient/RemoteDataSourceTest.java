package com.jiujiu.githubclient;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.LocalDatabase;
import com.jiujiu.githubclient.data.local.OwnerEntity;
import com.jiujiu.githubclient.data.remote.GithubApi;
import com.jiujiu.githubclient.data.remote.RemoteDataSource;
import com.jiujiu.githubclient.utils.InjectionUtil;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@RunWith(AndroidJUnit4.class)
public class RemoteDataSourceTest {
    private static final String TAG = "RemoteDataSourceTest";

    private LocalDatabase database;
    private LocalDataSource localDataSource;
    private LiveData<OwnerEntity> owner;
    private Observer<OwnerEntity> observer;
    private Context mContext;

    @Before
    public void init() {
        mContext = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(mContext, LocalDatabase.class).build();
        localDataSource = InjectionUtil.providesLocalDataSource();
    }

    @After
    public void closeBd() {
        database.close();
    }


    @Test
    public void testGithubApi() throws InterruptedException {
        Log.d(TAG, "testGithubApi: start");
        GithubApi githubApi = InjectionUtil.providesGithubApi();
        String owner = "my-jabin";
        CountDownLatch latch = new CountDownLatch(2);

        observer = (ownerEntity) -> {
            Log.d(TAG, "testGithubApi: onchanged");
            if (ownerEntity != null) {
                Log.d(TAG, "testGithubApi: owner= " + ownerEntity.getLogin());
                latch.countDown();
                this.owner.removeObserver(observer);
                Assert.assertThat(ownerEntity.getLogin(), new IsEqual(owner));
            }
        };
        this.owner = localDataSource.getOwnerByLogin(owner);
        this.owner.observeForever(observer);

        Disposable d = githubApi.getOwner(owner)
                .subscribeOn(Schedulers.io())
                .map(ownerResponse -> {
                    Log.d(TAG, "testGithubApi: mappping : " + (ownerResponse == null));
                    if (ownerResponse == null) return null;
                    else return ownerResponse.toOwnerEntity();
                })
                .flatMapCompletable(ownerEntity -> Completable.fromAction(() -> {
                    Log.d(TAG, "testGithubApi: flat map to completable");
                    if (ownerEntity != null) {
                        Log.d(TAG, "testGithubApi: insert the owner");
                        localDataSource.refreshOwner(ownerEntity);
                    }
                }))
//                .subscribe(() -> Log.d(TAG, "testGithubApi: success"),
//                        e -> Log.d(TAG, "testGithubApi: no user found " + e.getMessage()));
                .andThen(githubApi.getRepository(owner))
                .flatMapIterable(repositoryResponses -> repositoryResponses)
                .map(repositoryResponse -> repositoryResponse == null ? null : repositoryResponse.toRepositoryEntity())
                .flatMapCompletable(repositoryEntity -> Completable.fromAction(() -> {
                    if (repositoryEntity != null) {
                        Log.d(TAG, "testGithubApi: repository name: " + repositoryEntity.getName());
                        localDataSource.insertOrUpdate(repositoryEntity);
                    }
                }))
                .subscribe(() -> {
                            latch.countDown();
                            Log.d(TAG, "fetchRepository: success");
                        },
                        e -> Log.d(TAG, "testGithubApi: error  " + e.getMessage()));
        latch.await();
        d.dispose();
        Log.d(TAG, "testGithubApi: finish");
    }

    @Test
    public void testRemoteFetch() throws InterruptedException {
        RemoteDataSource remoteDataSource = InjectionUtil.providesRemoteDataSource();
        remoteDataSource.fetchRepository("my-jabin");

        owner = localDataSource.getOwnerByLogin("my-jabin");
        CountDownLatch latch = new CountDownLatch(1);

        observer = ownerEntity -> {
            if (ownerEntity != null) {
                latch.countDown();
                owner.removeObserver(observer);
                Log.d(TAG, "onChanged: owner: " + ownerEntity.getLogin());
            }
        };
        owner.observeForever(observer);
        latch.await(3, TimeUnit.SECONDS);
    }
}
