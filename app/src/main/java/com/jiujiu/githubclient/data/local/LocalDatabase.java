package com.jiujiu.githubclient.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {OwnerEntity.class, RepositoryEntity.class}, version = 1)
public abstract class LocalDatabase  extends RoomDatabase{

    private static final String TAG = "LocalDatabase";
    private static LocalDatabase mInstance;
    private static final String DATABASENAME = "githubclient.db";

    public abstract OwnerDao ownerDao();
    public abstract  RepositoryDao repositoryDao();

    public static LocalDatabase getInstance(Context context){
        Log.d(TAG, "getInstance: ");
        if(mInstance == null){
            synchronized (LocalDatabase.class){
                if(mInstance == null){
                    mInstance = Room.databaseBuilder(context,LocalDatabase.class, DATABASENAME).build();
                }
            }
        }
        return mInstance;
    }

}
