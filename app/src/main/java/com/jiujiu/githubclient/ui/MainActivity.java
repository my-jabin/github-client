package com.jiujiu.githubclient.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jiujiu.githubclient.R;
import com.jiujiu.githubclient.utils.Constant;
import com.jiujiu.githubclient.viewModel.MainActivityViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressBar mProgressBar;
    private MainActivityViewModel mViewModel;
    private Button mButton;
    // to avoid bugs, add this variable to prevent double openning activity.
    // todo: shoud move this variable.
    private boolean isProcessing = false;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mButton = findViewById(R.id.button);

        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getOwner().observe(this, ownerEntity -> {
            if (isProcessing) return;
            Log.d(TAG, "onCreate: owner is empty ? " + (ownerEntity == null));
            isProcessing = true;
            if (ownerEntity != null) {
                Log.d(TAG, "onCreate: show repository name =" + ownerEntity.getLogin());
                openRepository(ownerEntity.getLogin());
                mProgressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "The use does not exist", Toast.LENGTH_SHORT).show();
            }
            mButton.setEnabled(true);
            isProcessing = false;
        });

        mViewModel.getDeleteAllEvent().observe(this, (v) -> Toast.makeText(this, "All Records have been deleted", Toast.LENGTH_SHORT).show());
    }

    private void openRepository(String name) {
        Intent intent = new Intent(this, RepositoryListActivity.class);
        intent.putExtra(Constant.OWNERNAME, name);
        startActivity(intent);
    }

    public void showRepository(View view) {
        mButton.setEnabled(false);
        EditText editUserName = findViewById(R.id.et_userName);
        String userName = editUserName.getText().toString().trim();
        mProgressBar.setVisibility(View.VISIBLE);
        this.mViewModel.showRepository(userName);
        dismissKeyboard(view.getWindowToken());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deleteAll:
                this.mViewModel.deleteAllRecords();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dismissKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

}
