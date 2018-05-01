package com.jiujiu.githubclient.data.local;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalDataSource {

    private OwnerDao ownerDao;
    private RepositoryDao repositoryDao;

    @Inject
    public LocalDataSource(LocalDatabase database) {
        // LocalDatabase database = InjectionUtil.providesDatabase();
        this.ownerDao = database.ownerDao();
        this.repositoryDao = database.repositoryDao();
    }

    public void insertOrUpdate(RepositoryEntity repository) {
        repositoryDao.insertOrUpdate(repository);
    }

    public void refreshRepository(List<RepositoryEntity> list, String ownerName){
        repositoryDao.refreshRepo(list, ownerName);
    }

    public void refreshOwner(OwnerEntity owner) {
        ownerDao.insert(owner);
        ownerDao.update(owner);
    }

    public LiveData<OwnerEntity> getOwnerByLogin(String login) {
        return ownerDao.getOwnerByLogin(login);
    }

    public LiveData<List<RepositoryEntity>> getRepositoryByOwner(String owner) {
        return repositoryDao.getRepositoriesByOwner(owner);
    }

    public void deleteAllRecords() {
        ownerDao.deleteAll();
    }
}
