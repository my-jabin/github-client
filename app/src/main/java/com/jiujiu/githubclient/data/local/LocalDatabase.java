package com.jiujiu.githubclient.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {OwnerEntity.class, RepositoryEntity.class}, version = 1)
public abstract class LocalDatabase  extends RoomDatabase{
    public abstract OwnerDao ownerDao();
    public abstract  RepositoryDao repositoryDao();
}
