package com.jiujiu.githubclient.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface OwnerDao {

    @Query("select * from owner where login =:login")
    LiveData<OwnerEntity> getOwnerByLogin(String login);

    @Query("select count(*) from owner where login = :login")
    int existsOwner(String login);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(OwnerEntity owner);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(OwnerEntity ownerEntity);

    @Query("delete from owner")
    void deleteAll();
}
