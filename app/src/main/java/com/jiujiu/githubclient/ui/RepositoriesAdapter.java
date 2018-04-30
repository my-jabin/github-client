package com.jiujiu.githubclient.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiujiu.githubclient.R;
import com.jiujiu.githubclient.data.local.RepositoryEntity;
import com.jiujiu.githubclient.databinding.RepositoryItemBinding;

import java.util.List;
import java.util.Objects;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder> {
    private static final String TAG = "RepositoriesAdapter";
    private List<RepositoryEntity> mRepositories;

    public void setRepository(List<RepositoryEntity> repository) {
        if (repository == null || repository.size() == 0) return;
        if (mRepositories == null || mRepositories.size() == 0) {
            this.mRepositories = repository;
            notifyItemRangeInserted(0, repository.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mRepositories.size();
                }

                @Override
                public int getNewListSize() {
                    return repository.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return Objects.equals(mRepositories.get(oldItemPosition).getName(),
                            repository.get(newItemPosition).getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    RepositoryEntity oldRepo = mRepositories.get(oldItemPosition);
                    RepositoryEntity newRepo = repository.get(newItemPosition);
                    return Objects.equals(oldRepo.getName(), newRepo.getName()) &&
                            Objects.equals(oldRepo.getFullName(), newRepo.getFullName()) &&
                            Objects.equals(oldRepo.getCreatedAt(), newRepo.getCreatedAt()) &&
                            Objects.equals(oldRepo.getDescription(), newRepo.getDescription()) &&
                            Objects.equals(oldRepo.getForksCount(), newRepo.getForksCount()) &&
                            Objects.equals(oldRepo.getId(), newRepo.getId()) &&
                            Objects.equals(oldRepo.getLanguage(), newRepo.getLanguage()) &&
                            Objects.equals(oldRepo.getOwner(), newRepo.getOwner()) &&
                            Objects.equals(oldRepo.getSize(), newRepo.getSize()) &&
                            Objects.equals(oldRepo.getWatchersCount(), newRepo.getWatchersCount()) &&
                            Objects.equals(oldRepo.getPushedAt(), newRepo.getPushedAt());
                }
            });
            this.mRepositories = repository;
            result.dispatchUpdatesTo(this);
        }
        Log.d(TAG, "setRepository: finish");
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepositoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.repository_item, parent, false);
        return new RepositoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        if (mRepositories != null) {
            RepositoryEntity repository = mRepositories.get(position);
            holder.binding.tvRepoItemName.setText(repository.getName());
            holder.binding.tvRepoItemDesc.setText(repository.getDescription());
            if(!TextUtils.isEmpty(repository.getLanguage())){
                holder.binding.imageRepoItemLanguage.setVisibility(View.VISIBLE);
                holder.binding.tvLanguage.setVisibility(View.VISIBLE);
                holder.binding.tvLanguage.setText(repository.getLanguage());
            }
            holder.binding.tvStar.setText(String.valueOf(repository.getStargazersCount()));
            holder.binding.tvFork.setText(String.valueOf(repository.getForksCount()));
        }
    }

    @Override
    public int getItemCount() {
        return mRepositories == null ? 0 : mRepositories.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        RepositoryItemBinding binding;

        public RepositoryViewHolder(RepositoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

