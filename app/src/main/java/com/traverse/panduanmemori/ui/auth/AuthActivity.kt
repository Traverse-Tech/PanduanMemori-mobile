package com.traverse.panduanmemori.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.traverse.panduanmemori.ui.ViewModelFactory
import com.traverse.panduanmemori.ui.auth.screens.LoginScreen
import com.traverse.panduanmemori.ui.auth.screens.OnboardingScreen
import com.traverse.panduanmemori.ui.auth.screens.RegisterScreen
import com.traverse.panduanmemori.ui.auth.screens.RoleSelectionScreen
import com.traverse.panduanmemori.ui.components.themes.PanduanMemoriTheme
import com.traverse.panduanmemori.ui.home.HomeActivity

class AuthActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PanduanMemoriTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = ONBOARDING_SCREEN
                    ) {
                        composable(ONBOARDING_SCREEN) {
                            OnboardingScreen(navController, authViewModel)
                        }

                        composable(ROLE_SELECTION_SCREEN) {
                            RoleSelectionScreen(navController, authViewModel)
                        }

                        composable(LOGIN_SCREEN) {
                            LoginScreen(navController, authViewModel, onLoginSuccess = {
                                val intent = Intent(context, HomeActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                                finish()
                            })
                        }

                        composable(REGISTER_SCREEN) {
                            RegisterScreen(navController, authViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        customOnboardingBackButtonAction()
        super.onBackPressed()
    }

    private fun customOnboardingBackButtonAction() {
        if (authViewModel.onboardingCurrentIndex.value != 0)
            authViewModel.resetItem()
    }
}