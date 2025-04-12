package com.traverse.panduanmemori.ui.home.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.auth.AuthActivity
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.elements.*
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.ui.home.HomeViewModel
import com.traverse.panduanmemori.utils.AssetUtil
import com.traverse.panduanmemori.utils.AudioPlayer
import com.traverse.panduanmemori.utils.AudioRecorder
import com.traverse.panduanmemori.utils.StringUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

@Composable
fun HomeScreen(context: Context, homeViewModel: HomeViewModel, authViewModel: AuthViewModel) {
    val activity = context as Activity
    val userRole = authViewModel.getUser().role
    val audioRecorder = remember { AudioRecorder() }
    var isRecording by remember { mutableStateOf(false) }
    var recordedFilePath by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val buddyState by homeViewModel.buddyState.collectAsState()

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }


    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun requestAudioPermission() {
        if (!hasAudioPermission()) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }
    }

    LaunchedEffect(Unit) {
        if (homeViewModel.isShowWelcomeToast()) {
            toastMessage = "Selamat Datang!"
            toastDescription = "Berhasil masuk ke PanduanMemori"
            toastVisible = true
            homeViewModel.resetLoginContext()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(
                context = context,
                role = userRole,
                buddyOnPress = if (userRole == UserRole.PATIENT) {{
                    if (hasAudioPermission()) {
                        if (!isRecording) {
                            recordedFilePath = audioRecorder.startRecording()
                            isRecording = true
                        } else {
                            audioRecorder.stopRecording()
                            isRecording = false
                            coroutineScope.launch {
                                homeViewModel.buddyConversation(File(recordedFilePath))
                            }
                        }
                    } else {
                        requestAudioPermission()
                    }
                }} else null,
                isBuddyLoading = buddyState == ApiState.Loading
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
                CaregiverHomePage(
                    context,
                    homeViewModel,
                    authViewModel
                )
            } else {
                PatientHomePage(
                    context,
                    homeViewModel,
                    authViewModel,
                    isRecording
                )
            }
        }
    }
}
