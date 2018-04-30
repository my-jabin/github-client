package com.jiujiu.githubclient;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestUtil {

    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] objects = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer observer = new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                objects[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await();
        return (T) objects[0];
    }
}
