package com.alex.repositoryapp.ui.view.homeScreen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.repositoryapp.model.Repository
import com.alex.repositoryapp.repository.GitHubRepository
import com.alex.repositoryapp.selead.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var repoList by mutableStateOf<List<Repository>>(emptyList())
    var filteredRepoList by mutableStateOf<List<Repository>>(emptyList())
    var isLoading by mutableStateOf(false) // Tracks if a network call is in progress
    var errorMessage by mutableStateOf<String?>(null)
    private var currentPage = 1
    var isLastPage = false
    private var selectedLanguage: String? = null

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun fetchRepos(isNewSearch: Boolean = false) {
        if (isLoading || isLastPage) return // Prevent multiple calls if already loading or no more data

        viewModelScope.launch {
            isLoading = true
            errorMessage = null // Reset errorMessage before fetching data
            if (isNewSearch) {
                resetSearch() // Reset state for a new search
            }

            when (val response = repository.getRepositories(username, currentPage)) {
                is ApiResponse.Success -> {
                    val repos = response.data
                    if (repos.isNotEmpty()) {
                        repoList = repoList + repos
                        filterReposByLanguage(selectedLanguage) // Apply filter after fetching
                        currentPage++
                    } else {
                        isLastPage = true
                    }
                }

                is ApiResponse.Error -> {
                    errorMessage = response.errorMessage
                }
            }
            isLoading = false
        }
    }

    fun filterReposByLanguage(language: String?) {
        selectedLanguage = language
        filteredRepoList = if (language == null) {
            repoList
        } else {
            repoList.filter { it.language?.equals(language, ignoreCase = true) == true }
        }
    }
    private fun resetSearch() {
        currentPage = 1
        isLastPage = false
        repoList = emptyList()
        filteredRepoList = emptyList()
    }
}