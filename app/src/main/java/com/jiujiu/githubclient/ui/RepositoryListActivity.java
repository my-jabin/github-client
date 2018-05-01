package com.jiujiu.githubclient.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jiujiu.githubclient.R;
import com.jiujiu.githubclient.databinding.RepositoryListBinding;
import com.jiujiu.githubclient.utils.Constant;
import com.jiujiu.githubclient.viewModel.RepositoryListViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class RepositoryListActivity extends AppCompatActivity {

    private static final String TAG = "RepositoryListActivity";
    private RepositoryListViewModel viewModel;
    private RepositoryListBinding mBinding;
    private RepositoriesAdapter mAdapter;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.repository_list);
        mBinding.setIsLoading(true);
        mAdapter = new RepositoriesAdapter();
        mBinding.recyclerRespositories.setAdapter(mAdapter);

        setupViewModel();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        String userName = getIntent().getStringExtra(Constant.OWNERNAME);
        this.viewModel.startLoading(userName);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(RepositoryListViewModel.class);
        viewModel.getRepositories().observe(this, repositoryEntities -> {
            //todo: duplicated received repository entities. Reason?
            Log.d(TAG, " repository size = " + repositoryEntities.size());
            mBinding.setIsLoading(false);
            mBinding.setIsEmpty(repositoryEntities.size() == 0);
            mAdapter.setRepository(repositoryEntities);
        });
    }
}


