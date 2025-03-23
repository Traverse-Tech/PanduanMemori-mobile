package com.traverse.panduanmemori.ui.auth.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.auth.LOGIN_SCREEN
import com.traverse.panduanmemori.ui.components.elements.AppButton
import com.traverse.panduanmemori.ui.components.elements.ButtonSize
import com.traverse.panduanmemori.ui.components.elements.ButtonType
import com.traverse.panduanmemori.utils.AssetUtil
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.data.models.UserRole

@Composable
fun RoleSelectionScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    fun handleRoleButtonOnClick(role: UserRole) {
        viewModel.setUserRole(role)
        navController.navigate(LOGIN_SCREEN)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .widthIn(max = 600.dp)
                .align(Alignment.Center)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text ="Selamat Datang!",
                style = MaterialTheme.typography.h1
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .aspectRatio(1.318f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = AssetUtil.getAssetUrl("momo_greetings.png")),
                    contentDescription = "Welcome to PanduanMemori",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text ="Kamu akan masuk sebagai apa?",
                style = MaterialTheme.typography.h3
            )

            Column {
                AppButton(
                    text = "Pasien Demensia",
                    size = ButtonSize.LARGE,
                    type = ButtonType.CYAN,
                    onClick = { handleRoleButtonOnClick(UserRole.PATIENT) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_elder)
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppButton(
                    text = "Caregiver",
                    size = ButtonSize.LARGE,
                    onClick = { handleRoleButtonOnClick(UserRole.CAREGIVER) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_health_care)
                )
            }
        }
    }
}
