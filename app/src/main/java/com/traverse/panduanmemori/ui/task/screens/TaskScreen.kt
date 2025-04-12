package com.traverse.panduanmemori.ui.task.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.elements.AppBottomBar
import com.traverse.panduanmemori.ui.task.TaskViewModel

@Composable
fun TaskScreen(context: Context, taskViewModel: TaskViewModel, authViewModel: AuthViewModel) {
    val userRole = authViewModel.getUser().role

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(
                context = context,
                role = userRole,
                buddyOnPress = {}
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (userRole == UserRole.CAREGIVER) {
                CaregiverTaskPage(taskViewModel, authViewModel)
            } else {
                PatientTaskPage()
            }
        }
    }
}