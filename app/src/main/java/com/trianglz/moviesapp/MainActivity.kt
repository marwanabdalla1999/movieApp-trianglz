package com.trianglz.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.trianglz.moviesapp.ui.theme.MoviesAppTheme
import com.trianglz.ui.commonUi.LocalAppSnackBarHostState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                CompositionLocalProvider(LocalAppSnackBarHostState provides snackBarHostState) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(snackBarHostState) },
                    ) { innerPadding ->
                        NavigationHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                }

            }
        }
    }
}

