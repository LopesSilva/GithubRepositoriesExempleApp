package com.example.githubrepositoriesapp.api

import com.example.githubrepositoriesapp.data.model.PullRequestResponse
import com.example.githubrepositoriesapp.data.model.RepoSearchResponse
import org.intellij.lang.annotations.Language
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GitHubEndpointAPI {
    @GET("search/repositories")
    suspend fun getRepoByLanguage(
                                  @Query("q")language: String? = "language:Java",
                                  @Query("sort")sort:String?= "stars",
                                  @Query("page") page: Int,
                                  @Query("per_page") perPage : Int,
                                  ): RepoSearchResponse

    @GET("repos/{login}/{repository}/pulls")
    suspend fun getPullRequests(@Path("login") login:String,@Path("repository")repository:String):List<PullRequestResponse>

}