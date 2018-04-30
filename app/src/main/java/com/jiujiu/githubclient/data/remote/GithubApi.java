package com.jiujiu.githubclient.data.remote;

import com.jiujiu.githubclient.data.remote.model.OwnerResponse;
import com.jiujiu.githubclient.data.remote.model.RepositoryResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApi {

    @GET("/users/{userName}")
    Single<OwnerResponse> getOwner(@Path("userName") String userName);

    @GET("/users/{userName}/repos?per_page=50")
    Observable<List<RepositoryResponse>> getRepository(@Path("userName") String userName);

//    @GET("/users/{userName}")
//    LiveData<ApiResponse<OwnerResponse>> getOwner(@Path("userName") String userName);
//
//    @GET("/users/{userName}/repos")
//    LiveData<ApiResponse<List<RepositoryResponse>>> getRepository(@Path("userName") String userName);

}
