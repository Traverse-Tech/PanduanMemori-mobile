package com.traverse.panduanmemori.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.ui.auth.AuthActivity
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.ui.components.themes.PanduanMemoriTheme
import com.traverse.panduanmemori.ui.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    private val authViewModel by viewModels<AuthViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PanduanMemoriTheme {
                val context = LocalContext.current
                val isAuthenticated by authViewModel.isAuthenticated.asFlow().collectAsState(initial = false)

                LaunchedEffect(Unit) {
                    authViewModel.checkAuthentication()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SplashScreen(mainViewModel, onFinish = {
                        val intent = if (isAuthenticated) {
                            Intent(context, HomeActivity::class.java)
                        } else {
                            Intent(context, AuthActivity::class.java)
                        }

                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(mainViewModel: MainViewModel, onFinish: () -> Unit) {
    val showSplashSecondImage by mainViewModel.showSplashSecondImage.asFlow().collectAsState(initial = false)
    val showSplash by mainViewModel.showSplash.asFlow().collectAsState(initial = true)

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF075753), AppColors.Primary.Main),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val imageSize: Dp = if (screenWidth > 600.dp) {
        500.dp
    } else {
        screenWidth * 0.8f
    }

    LaunchedEffect(showSplash) {
        if (!showSplash) {
            onFinish()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedContent(targetState = showSplashSecondImage, transitionSpec = {
            fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(500))
        }) { state ->
            if (state) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradientBrush)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.Primary.Main)
                )
            }
        }

        AnimatedContent(targetState = showSplashSecondImage, transitionSpec = {
            fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(500))
        }) { state ->
            if (state) {
                Image(
                    painter = painterResource(id = R.drawable.splash_main),
                    contentDescription = "Splash 1",
                    modifier = Modifier
                        .size(imageSize)
                        .align(Alignment.Center)
                )

                Image(
                    painter = painterResource(id = R.drawable.splash_secondary),
                    contentDescription = "Splash 2",
                    modifier = Modifier
                        .size(imageSize)
                        .align(Alignment.Center)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.splash_main),
                    contentDescription = "Splash 1",
                    modifier = Modifier
                        .size(imageSize)
                )
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val _showSplash = MutableLiveData(true)
    val showSplash: LiveData<Boolean> get() = _showSplash

    private val _showSplashSecondImage = MutableLiveData(false)
    val showSplashSecondImage: LiveData<Boolean> get() = _showSplashSecondImage

    init {
        viewModelScope.launch {
            delay(5000)
            _showSplash.value = false
        }

        viewModelScope.launch {
            delay(2500)
            _showSplashSecondImage.value = true
        }
    }
}
