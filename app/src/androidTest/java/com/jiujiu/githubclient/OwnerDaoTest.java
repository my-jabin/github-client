package com.jiujiu.githubclient;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.text.TextUtils;

import com.jiujiu.githubclient.data.local.LocalDataSource;
import com.jiujiu.githubclient.data.local.LocalDatabase;
import com.jiujiu.githubclient.data.local.OwnerDao;
import com.jiujiu.githubclient.data.local.OwnerEntity;
import com.jiujiu.githubclient.utils.InjectionUtil;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OwnerDaoTest {

    private Context mContext;
    private LocalDatabase database;
    private OwnerDao ownerDao;

    @Before
    public void init() {
        mContext = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(mContext, LocalDatabase.class).build();
        ownerDao = database.ownerDao();
    }

    @Test
    public void testInsert() throws InterruptedException {
        OwnerEntity entity = new OwnerEntity();
        entity.setLogin("My-jabin");
        entity.setId(1);
        ownerDao.insertOrUpdate(entity);

        Assert.assertEquals( ownerDao.existsOwner("My-jabin"),1);

       LiveData<OwnerEntity> ownerLiveData =  ownerDao.getOwnerByLogin("My-jabin");

        OwnerEntity owner =  TestUtil.getValue(ownerLiveData);
        Assert.assertThat(owner.getLogin(), new IsEqual<String>("My-jabin"));
    }

    @After
    public void closeBd() {
        database.close();
    }

}
