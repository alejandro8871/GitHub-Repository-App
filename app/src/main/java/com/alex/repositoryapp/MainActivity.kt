package com.alex.repositoryapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.material3.Scaffold
import com.alex.repositoryapp.ui.theme.RepositoryAppTheme
import com.alex.repositoryapp.ui.view.homeScreen.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepositoryAppTheme {
                Scaffold { innerPadding ->
                    HomeScreen(innerPadding)
                }
            }
        }
    }
}