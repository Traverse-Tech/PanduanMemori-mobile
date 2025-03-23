package com.traverse.panduanmemori.ui.home.screens

import android.util.Log
import androidx.compose.runtime.*
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.elements.AppButton
import com.traverse.panduanmemori.ui.components.elements.AppToast
import com.traverse.panduanmemori.ui.components.elements.ButtonSize
import com.traverse.panduanmemori.ui.components.elements.ToastVariant
import com.traverse.panduanmemori.ui.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, authViewModel: AuthViewModel) {
    val coroutineScope = rememberCoroutineScope()

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (homeViewModel.isShowWelcomeToast()) {
            toastMessage = "Selamat Datang!"
            toastDescription = "Berhasil masuk ke PanduanMemori"
            toastVisible = true
            homeViewModel.resetLoginContext()
        }
    }

    AppToast(
        message = toastMessage ?: "",
        description = toastDescription,
        variant = ToastVariant.SUCCESS,
        isVisible = toastVisible,
        onDismiss = { toastVisible = false }
    )

    AppButton(text = "Keluar", size = ButtonSize.LARGE, onClick = {
        coroutineScope.launch {
            authViewModel.logout()
        }
    })
}
