package com.jiujiu.githubclient.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.util.Log;

import java.util.List;

@Dao
public abstract class RepositoryDao {
    private static final String TAG = "RepositoryDao";

    @Query("select * from repositories where owner = :owner order by stargazersCount desc")
    abstract LiveData<List<RepositoryEntity>> getRepositoriesByOwner(String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertOrUpdate(RepositoryEntity entities);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void bulkInsert(List<RepositoryEntity> list);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract void bulkUpdate(List<RepositoryEntity> list);

    @Query("delete from repositories where owner = :owner")
    abstract void delete(String owner);

    @Transaction
    void refreshRepo(List<RepositoryEntity> list, String ownerName){
        Log.d(TAG, "refreshRepo: start");
        delete(ownerName);
        bulkInsert(list);
        Log.d(TAG, "refreshRepo: finish");
    }
}
