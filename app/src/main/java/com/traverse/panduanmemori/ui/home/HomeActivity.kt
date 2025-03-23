package com.traverse.panduanmemori.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.traverse.panduanmemori.ui.ViewModelFactory
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.themes.PanduanMemoriTheme
import com.traverse.panduanmemori.ui.home.screens.HomeScreen

class HomeActivity : ComponentActivity() {
    private val homeViewModel by viewModels<HomeViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    private val authViewModel by viewModels<AuthViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PanduanMemoriTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = HOME_SCREEN
                    ) {
                        composable(HOME_SCREEN) {
                            HomeScreen(homeViewModel, authViewModel)
                        }
                    }
                }
            }
        }
    }
}
