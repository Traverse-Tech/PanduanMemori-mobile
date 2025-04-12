package com.traverse.panduanmemori.ui.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.auth.ROLE_SELECTION_SCREEN
import com.traverse.panduanmemori.ui.components.elements.AppButton
import com.traverse.panduanmemori.ui.components.elements.ButtonSize
import com.traverse.panduanmemori.ui.components.elements.ButtonType
import com.traverse.panduanmemori.ui.components.elements.ButtonVariant
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.utils.AssetUtil

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val onboardingItems by viewModel.onboardingItems.collectAsState()
    val onboardingCurrentIndex = viewModel.onboardingCurrentIndex.value
    val onboardingCurrentItem = onboardingItems[onboardingCurrentIndex]

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
                .widthIn(max = 600.dp)
                .align(Alignment.Center)
                .fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (index in onboardingItems.indices) {
                        BulletPoint(isActive = index == onboardingCurrentIndex)
                    }
                }

                AppButton(
                    text = "Lewati",
                    onClick = { navController.navigate(ROLE_SELECTION_SCREEN) },
                    variant = ButtonVariant.TEXT_ONLY,
                    type = ButtonType.CYAN,
                    size = ButtonSize.SMALL
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.742f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = AssetUtil.getAssetUrl(onboardingCurrentItem.imageUrl)),
                    contentDescription = onboardingCurrentItem.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(text = onboardingCurrentItem.title, style = MaterialTheme.typography.h2)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = onboardingCurrentItem.description, style = MaterialTheme.typography.body1)
            }

            Spacer(modifier = Modifier.weight(1f))
            AppButton(
                text = "Selanjutnya",
                onClick = {
                    if (onboardingCurrentIndex == onboardingItems.size - 1)
                        navController.navigate(ROLE_SELECTION_SCREEN)
                    else
                        viewModel.nextItem()
                },
                modifier = Modifier.fillMaxWidth(),
                type = ButtonType.CYAN,
                size = ButtonSize.LARGE
            )
        }
    }
}

@Composable
fun BulletPoint(isActive: Boolean) {
    val color = if (isActive) AppColors.Primary.Main else AppColors.Primary.`100`

    Box(
        modifier = Modifier
            .width(if (isActive) 36.dp else 16.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color)
    )
}
