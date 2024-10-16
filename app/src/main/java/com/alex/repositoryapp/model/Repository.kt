package com.alex.repositoryapp.model

data class Repository(
    val id: Int,
    val name: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val owner: Owner,
    val updated_at: String
)


data class Owner(
    val login: String
)