package com.traverse.panduanmemori.ui.location.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.elements.AppBottomBar
import com.traverse.panduanmemori.ui.components.elements.AppToast
import com.traverse.panduanmemori.ui.components.elements.ToastVariant
import com.traverse.panduanmemori.ui.home.HomeActivity

@Composable
fun LocationScreen(context: Context, authViewModel: AuthViewModel) {
    val activity = context as Activity
    val userRole = authViewModel.getUser().role

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(
                context = context,
                role = userRole,
                buddyOnPress = if (userRole == UserRole.PATIENT) {{
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                    (context as? Activity)?.overridePendingTransition(0, 0)
                    (context as? Activity)?.finish()
                }} else null,
                isBuddyLoading = false
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppToast(
                message = toastMessage ?: "",
                description = toastDescription,
                variant = ToastVariant.SUCCESS,
                isVisible = toastVisible,
                onDismiss = { toastVisible = false }
            )

            if (userRole == UserRole.CAREGIVER) {
                CaregiverLocationPage()
            } else {
                PatientLocationPage(context)
            }
        }
    }
}