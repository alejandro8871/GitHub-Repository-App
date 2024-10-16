package com.alex.repositoryapp.retrofit

import com.alex.repositoryapp.model.Repository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30 // Default to 30 repos per page
    ): List<Repository>
}