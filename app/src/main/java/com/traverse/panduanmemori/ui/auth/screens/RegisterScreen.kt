package com.traverse.panduanmemori.ui.auth.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.traverse.panduanmemori.ui.auth.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    Text(text = "REGISTER")
}
