package com.traverse.panduanmemori.ui.home.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.traverse.panduanmemori.utils.AudioRecorder
import com.traverse.panduanmemori.utils.StringUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

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

    AppToast(
        message = toastMessage ?: "",
        description = toastDescription,
        variant = ToastVariant.SUCCESS,
        isVisible = toastVisible,
        onDismiss = { toastVisible = false }
    )

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
    ) {
        if (userRole == UserRole.CAREGIVER) {
            CaregiverHomePage(
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

@Composable
fun PatientHomePage(context: Context, homeViewModel: HomeViewModel, authViewModel: AuthViewModel, isRecording: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    val getUser = authViewModel.getUser()
    val initialName = getUser.name.first().toString().uppercase()
    val randomColor = StringUtil.getRandomColorFromString(initialName)
    val textColor = if (randomColor.red + randomColor.green + randomColor.blue > 1.5f) AppColors.Neutral.`110` else AppColors.Neutral.`10`

    val buddyState by homeViewModel.buddyState.collectAsState()

    var buddyDefaultText by remember { mutableStateOf("Halo! Kenalin aku Nana, Aku siap membantu dan selalu menemani Bapak!") }

    LaunchedEffect(Unit) {
        delay(5000)
        buddyDefaultText = "Silakan tahan tombol mikrofon pada layar untuk mulai ngobrol sama Nana, ya!"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PanduanMemori",
                style = MaterialTheme.typography.subtitle1,
                color = AppColors.Primary.Main
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .background(color = randomColor, shape = CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            authViewModel.logout()
                        }
                        val intent = Intent(context, AuthActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.overridePendingTransition(0, 0)
                        (context as? Activity)?.finish()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialName,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .aspectRatio(1f)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = AssetUtil.getAssetUrl("nana.png")),
                        contentDescription = "Buddy",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.Primary.`100`)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (buddyState == ApiState.Success) homeViewModel.buddyConversationResponse.value ?: "" else if (buddyState == ApiState.Loading) "Memproses..." else if (isRecording) "Mendengarkan" else buddyDefaultText,
                        style = MaterialTheme.typography.body1.copy(fontStyle = if (buddyState == ApiState.Loading || isRecording) FontStyle.Italic else FontStyle.Normal),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun CaregiverHomePage(homeViewModel: HomeViewModel, authViewModel: AuthViewModel) {
    val coroutineScope = rememberCoroutineScope()

    AppButton(text = "Keluar", size = ButtonSize.LARGE, onClick = {
        coroutineScope.launch {
            authViewModel.logout()
        }
    })
}
