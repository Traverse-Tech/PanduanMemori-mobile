package com.traverse.panduanmemori.ui.location

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.traverse.panduanmemori.ui.ViewModelFactory
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.themes.PanduanMemoriTheme
import com.traverse.panduanmemori.ui.location.screens.LocationScreen

class LocationActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val locationPermissionRequestCode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasLocationPermission()) {
            requestPermissions(requiredPermissions, locationPermissionRequestCode)
        } else {
            showLocationScreen()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return requiredPermissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun showLocationScreen() {
        setContent {
            PanduanMemoriTheme {
                val context = this@LocationActivity

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    LocationScreen(context, authViewModel)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionRequestCode &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            showLocationScreen()
        }
    }
}