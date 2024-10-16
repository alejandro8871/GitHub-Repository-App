package com.alex.repositoryapp.ui.view.homeScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alex.repositoryapp.model.Repository

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(paddingValues: PaddingValues, viewModel: RepoViewModel = hiltViewModel()) {
    var usernameInput by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) } // Flag to track if user initiated search


    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Text field to enter GitHub username
        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text("GitHub Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search button and filter button
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    // Trigger fetching repositories when the "Search" button is clicked
                    if (usernameInput.isNotBlank()) {
                        viewModel.username = usernameInput
                        viewModel.fetchRepos(isNewSearch = true) // Only search when triggered by user
                        hasSearched = true
                    }
                }
            ) {
                Text("Search")
            }

            IconButton(onClick = { showFilterDialog = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display loading spinner or repository list based on state
        if (viewModel.isLoading && viewModel.repoList.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            RepoList(repositories = viewModel.filteredRepoList, viewModel, hasSearched)

            if (viewModel.errorMessage != null) {
                Text(text = viewModel.errorMessage!!, color = Color.Red)
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onFilterSelected = { selectedLanguage ->
                viewModel.filterReposByLanguage(selectedLanguage)
            }
        )
    }
}

@SuppressLint("NewApi")
@Composable
fun RepoList(
    repositories: List<Repository>, viewModel: RepoViewModel, hasSearched: Boolean
) {
    val listState = rememberLazyListState() // Track the state of the list
    var expandedRepoId by remember { mutableStateOf<Int?>(null) }

    LazyColumn(state = listState) {
        items(repositories) { repo ->
            val isExpanded = expandedRepoId == repo.id

            RepoListItem(
                repository = repo,
                isExpanded = isExpanded,
                onExpandToggle = {
                    expandedRepoId = if (isExpanded) null else repo.id
                }
            )
        }

        item {
            if (viewModel.isLoading && viewModel.repoList.isNotEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }

    // Detect if user scrolled to the end of the list
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        }.collect { lastVisibleItemIndex ->
            // Trigger pagination if the user reaches the last item
            if (hasSearched && lastVisibleItemIndex >= repositories.size - 1 && !viewModel.isLastPage && !viewModel.isLoading) {
                viewModel.fetchRepos()
            }
        }
    }
}

@Composable
fun RepoListItem(
    repository: Repository,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandToggle() }
            .padding(16.dp)
    ) {
        // Basic information (Always visible)
        Text(text = repository.name, style = MaterialTheme.typography.titleMedium)
        repository.description?.let {
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = "Language: ${repository.language ?: "Unknown"}")
        Text(text = "Stars: ${repository.stargazers_count}")
        Text(text = "Forks: ${repository.forks_count}")

        // Expandable section (only visible when expanded)
        AnimatedVisibility(visible = isExpanded) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                // Owner information
                Text(text = "Owner: ${repository.owner.login}")

                // Last updated date
                Text(text = "Last Updated: ${repository.updated_at}")
            }
        }
    }
}