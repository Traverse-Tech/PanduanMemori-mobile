package com.traverse.panduanmemori.ui.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.traverse.panduanmemori.ui.components.elements.*
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.auth.*
import com.traverse.panduanmemori.ui.components.themes.AppColors

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val userRole = viewModel.getUserRole()
    val userRoleDescription = if (userRole == UserRole.PATIENT) "Pasien Demensia" else "Caregiver"

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()
    val authenticatedState by viewModel.authenticatedState.collectAsState()

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is ApiState.Success -> {
                if (authenticatedState == AuthenticatedState.Unassigned)
                    navController.navigate(PATIENT_PROFILE_SCREEN)
                else
                    onLoginSuccess()
            }
            is ApiState.Error -> {
                val errorState = loginState as ApiState.Error
                toastMessage = errorState.message
                toastDescription = errorState.description
                toastVisible = true
                viewModel.setLoginState(ApiState.Idle)
            }
            else -> {}
        }
    }

    AppToast(
        message = toastMessage ?: "",
        description = toastDescription,
        variant = ToastVariant.DANGER,
        isVisible = toastVisible,
        onDismiss = { toastVisible = false }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AppButton(
            onClick = { navController.popBackStack() },
            icon = Icons.Filled.Close,
            iconPosition = ButtonIconPosition.ONLY,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            type = if (userRole == UserRole.PATIENT) ButtonType.CYAN else ButtonType.PRIMARY,
            size = ButtonSize.SMALL,
            variant = ButtonVariant.SECONDARY
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Column {
                AppTag(
                    text = userRoleDescription,
                    icon = ImageVector.vectorResource(id = if (userRole == UserRole.PATIENT) R.drawable.ic_elder else R.drawable.ic_health_care),
                    type = TagType.OUTLINED,
                    variant = if (userRole == UserRole.CAREGIVER) TagVariant.INFO else TagVariant.PRIMARY
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Selamat Datang!",
                    style = MaterialTheme.typography.h1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Masukkan NIK atau email beserta password untuk masuk sebagai ${userRoleDescription.lowercase()}",
                    style = MaterialTheme.typography.body1
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            Column {
                AppTextInput(
                    value = identifier,
                    onValueChange = { identifier = it },
                    title = "NIK / Email",
                    placeholder = "Isi antara NIK atau Email kamu",
                    leftIcon = Icons.Filled.Email,
                    leftIconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppTextInput(
                    value = password,
                    onValueChange = { password = it },
                    title = "Password",
                    placeholder = "Isi password dari akun kamu",
                    leftIcon = Icons.Filled.Lock,
                    leftIconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`,
                    type = KeyboardType.Password
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            AppButton(
                onClick = {
                    viewModel.login(identifier, password)
                },
                text = "Masuk",
                size = ButtonSize.LARGE,
                type = if (userRole == UserRole.PATIENT) ButtonType.CYAN else ButtonType.PRIMARY,
                modifier = Modifier.fillMaxWidth(),
                disabled = identifier.isEmpty() || password.isEmpty(),
                isLoading = loginState == ApiState.Loading
            )

            Spacer(modifier = Modifier.height(20.dp))
            
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Belum punya akun?",
                        style = MaterialTheme.typography.body1
                    )
                    AppButton(
                        text = "Registrasi di sini",
                        onClick = { navController.navigate(REGISTER_SCREEN) },
                        variant = ButtonVariant.TEXT_ONLY,
                        type = if (userRole == UserRole.CAREGIVER) ButtonType.PRIMARY else ButtonType.CYAN,
                        size = ButtonSize.SMALL,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}
