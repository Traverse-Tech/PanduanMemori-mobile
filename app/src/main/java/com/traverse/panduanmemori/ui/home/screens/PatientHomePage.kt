package com.traverse.panduanmemori.ui.home.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.auth.AuthActivity
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.ui.home.HomeViewModel
import com.traverse.panduanmemori.utils.AssetUtil
import com.traverse.panduanmemori.utils.AudioPlayer
import com.traverse.panduanmemori.utils.StringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

@Composable
fun PatientHomePage(context: Context, homeViewModel: HomeViewModel, authViewModel: AuthViewModel, isRecording: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    val getUser = authViewModel.getUser()
    val initialName = getUser.name.first().toString().uppercase()
    val randomColor = StringUtil.getRandomColorFromString(initialName)
    val textColor = if (randomColor.red + randomColor.green + randomColor.blue > 1.5f) AppColors.Neutral.`110` else AppColors.Neutral.`10`

    val buddyConversationResponse by homeViewModel.buddyConversationResponse.collectAsState()
    val buddyState by homeViewModel.buddyState.collectAsState()

    val audioPlayer = remember { AudioPlayer() }
    var buddyDefaultText by remember { mutableStateOf("Halo! Kenalin aku Nana, Aku siap membantu dan selalu menemani Bapak!") }

    LaunchedEffect(buddyState) {
        if (buddyState == ApiState.Success) {
            val audioUrl = buddyConversationResponse.audioPath.replaceFirst("gs://", "https://storage.googleapis.com/")
            val localFile = File(context.cacheDir, audioUrl.substringAfterLast("/"))

            withContext(Dispatchers.IO) {
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(audioUrl)
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            val inputStream = response.body?.byteStream()
                            val outputStream = FileOutputStream(localFile)

                            inputStream?.copyTo(outputStream)
                            inputStream?.close()
                            outputStream.close()
                        } else {
                            Log.e("AudioPlayer", "Gagal mendownload file audio: ${response.message}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AudioPlayer", "Error download audio: ${e.message}", e)
                }
            }

            // Setelah download selesai, baru play di main thread
            audioPlayer.playAudioFromFile(localFile) {
                homeViewModel.setBuddyFollowUpText()
            }
        }
    }



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

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.Primary.`100`)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (buddyState == ApiState.Success) buddyConversationResponse.transcript ?: "" else if (buddyState == ApiState.Loading) "Memproses..." else if (isRecording) "Mendengarkan" else buddyDefaultText,
                        style = MaterialTheme.typography.body1.copy(fontStyle = if (buddyState == ApiState.Loading || isRecording) FontStyle.Italic else FontStyle.Normal),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
