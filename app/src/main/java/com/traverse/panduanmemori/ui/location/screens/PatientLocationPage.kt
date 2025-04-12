package com.traverse.panduanmemori.ui.location.screens

import android.content.Context
import android.content.Intent
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.traverse.panduanmemori.ui.components.elements.*
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.gms.location.LocationServices
import com.traverse.panduanmemori.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine

@SuppressLint("MissingPermission")
@Composable
fun PatientLocationPage(context: Context) {
    val safeLocation = LatLng(-6.201000, 106.816000)
    var userLocation by remember { mutableStateOf(LatLng(-6.200000, 106.816666)) }
    var userAddress by remember { mutableStateOf("Mencari lokasi...") }
    var safeLocationState by remember { mutableStateOf(true) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    fun playAlertSound(context: Context) {
        mediaPlayer.value?.release()
        val newPlayer = MediaPlayer.create(context, R.raw.alert_sound)
        mediaPlayer.value = newPlayer
        newPlayer.setOnCompletionListener {
            it.release()
            mediaPlayer.value = null
        }
        newPlayer.start()
    }

    suspend fun getLastLocation(context: Context): Location? = suspendCancellableCoroutine { cont ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location -> cont.resume(location, null) }
            .addOnFailureListener { cont.resume(null, null) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val location = getLastLocation(context)
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                userLocation = userLatLng
                cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 15f)

                val geocoder = Geocoder(context)
                try {
                    val addresses = geocoder.getFromLocation(userLatLng.latitude, userLatLng.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        userAddress = addresses[0].getAddressLine(0)
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> Log.e("ERROR NIH", it1) }
                    toastMessage = "Gagal mengambil alamant"
                    toastVisible = true
                }

                val userLoc = Location("").apply {
                    latitude = userLatLng.latitude
                    longitude = userLatLng.longitude
                }
                val safeLoc = Location("").apply {
                    latitude = safeLocation.latitude
                    longitude = safeLocation.longitude
                }

                val distance = userLoc.distanceTo(safeLoc)

                if (distance > 100) {
                    playAlertSound(context)
                    safeLocationState = false
                } else {
                    safeLocationState = true
                }
            }

            delay(20000)
        }
    }

    // Toast to show message
    AppToast(
        message = toastMessage ?: "",
        variant = ToastVariant.DANGER,
        isVisible = toastVisible,
        onDismiss = { toastVisible = false }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppTag(
                    text = if (safeLocationState) "Di dalam lokasi aman" else "Di luar lokasi aman, segera kembali",
                    type = TagType.OUTLINED,
                    icon = if (safeLocationState) null else Icons.Default.Warning,
                    variant = if (safeLocationState) TagVariant.PRIMARY else TagVariant.DANGER
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = AppColors.Danger.`40`,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color(0x40F7D8D4))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userAddress,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            AppButton(
                text = "Telepon Pengawas",
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:081245353137")
                    }
                    context.startActivity(intent)
                },
                type = ButtonType.ORANGE,
                size = ButtonSize.SMALL,
                icon = Icons.Filled.Call,
                iconPosition = ButtonIconPosition.LEFT
            )
        }
    }
}