package com.jiujiu.githubclient.data.local;

import android.arch.lifecycle.LiveData;

import com.jiujiu.githubclient.utils.InjectionUtil;

import java.util.List;

public class LocalDataSource {

    private OwnerDao ownerDao;
    private RepositoryDao repositoryDao;
    private static LocalDataSource mInstance;

    public static LocalDataSource getInstance() {
        if (mInstance == null) {
            mInstance = new LocalDataSource();
        }
        return mInstance;
    }

    //todo: inject local database
    private LocalDataSource() {
        LocalDatabase database = InjectionUtil.providesDatabase();
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
