package com.jiujiu.githubclient.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.jiujiu.githubclient.data.DataRepository;
import com.jiujiu.githubclient.data.local.OwnerEntity;
import com.jiujiu.githubclient.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private DataRepository mDataRepository;
    private MediatorLiveData<OwnerEntity> owner = new MediatorLiveData<>();
    private LiveData<OwnerEntity> currentOwner;
    private SingleLiveEvent<Void> deleteAllEvent = new SingleLiveEvent<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable( );

    @Inject
    public MainActivityViewModel(DataRepository dataRepository) {
        this.mDataRepository = dataRepository;
    }

    public LiveData<Void> getDeleteAllEvent() {
        return deleteAllEvent;
    }

    public LiveData<OwnerEntity> getOwner() {
        return owner;
    }

    public void showRepository(String ownerName) {
        owner.removeSource(currentOwner);
        currentOwner = mDataRepository.getOwner(ownerName);
        owner.addSource(currentOwner, ownerEntity -> owner.setValue(ownerEntity));
    }

    public void deleteAllRecords() {
        Disposable d = Completable.fromRunnable(() -> mDataRepository.deleteAllRecords())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> deleteAllEvent.callOnThread());
        compositeDisposable.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.compositeDisposable.dispose();
    }
}
