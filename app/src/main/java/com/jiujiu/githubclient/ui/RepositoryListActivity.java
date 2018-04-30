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
import com.jiujiu.githubclient.viewModel.RepositoryListViewModelFactory;

public class RepositoryListActivity extends AppCompatActivity {

    private static final String TAG = "RepositoryListActivity";
    private RepositoryListViewModel viewModel;
    private RepositoryListBinding mBinding;
    private RepositoriesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.repository_list);
        mBinding.setIsLoading(true);
        mAdapter = new RepositoriesAdapter();
        mBinding.recyclerRespositories.setAdapter(mAdapter);

        setupViewModel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.viewModel.startLoading();
    }

    private void setupViewModel() {
        String userName = getIntent().getStringExtra(Constant.OWNERNAME);
        ViewModelProvider.Factory factory = new RepositoryListViewModelFactory(userName);
        viewModel = ViewModelProviders.of(this, factory).get(RepositoryListViewModel.class);
        viewModel.getRepositories().observe(this, repositoryEntities -> {
            Log.d(TAG, "onCreate:  repository size = " + repositoryEntities.size());
            mBinding.setIsLoading(false);
            mBinding.setIsEmpty(repositoryEntities.size() == 0);
            mAdapter.setRepository(repositoryEntities);
        });
    }
}


