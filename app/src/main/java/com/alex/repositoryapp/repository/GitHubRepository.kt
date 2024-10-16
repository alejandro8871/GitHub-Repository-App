package com.alex.repositoryapp.repository

import retrofit2.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.alex.repositoryapp.model.Repository
import com.alex.repositoryapp.retrofit.GitHubService
import com.alex.repositoryapp.selead.ApiResponse
import java.io.IOException
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val gitHubService: GitHubService) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun getRepositories(
        username: String,
        page: Int,
        perPage: Int = 30
    ): ApiResponse<List<Repository>> {
        return try {
            val response = gitHubService.getRepos(username, page, perPage)
            ApiResponse.Success(response)
        } catch (e: HttpException) {
            when (e.code()) {
                403, 429 -> {
                    // Handle rate limit exceeded errors
                    ApiResponse.Error("API Rate Limit Exceeded. Please try again later.")
                }
                else -> {
                    // Handle other HTTP exceptions
                    ApiResponse.Error("An error occurred: ${e.message}")
                }
            }
        } catch (e: IOException) {
            // Handle network issues like no connectivity
            ApiResponse.Error("Network error. Please check your connection and try again.")
        } catch (e: Exception) {
            // Handle any other type of errors
            ApiResponse.Error("An unexpected error occurred.")
        }
    }
}