package com.traverse.panduanmemori.ui.components.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.traverse.panduanmemori.ui.components.themes.AppColors
import kotlinx.coroutines.delay
import com.traverse.panduanmemori.R

// CONSTANTS
enum class ToastVariant {
    SUCCESS, DANGER, INFO
}


// COMPONENTS
@Composable
fun AppToast(
    message: String,
    variant: ToastVariant = ToastVariant.SUCCESS,
    description: String? = null,
    duration: Long = 5000L,
    onDismiss: (() -> Unit)? = null,
    isVisible: Boolean = false
) {
    val (backgroundColor, messageColor, descriptionColor) = getToastColorSet(variant)
    var visible by remember { mutableStateOf(isVisible) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            visible = true
            delay(duration)
            visible = false
            delay(300)
            onDismiss?.invoke()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.zIndex(1000f)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            elevation = 2.dp,
            color = backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start) // <-- ini dia biar secukupnya
                    .fillMaxWidth() // supaya tetap nempel kiri kanan kalau butuh
                    .border(1.dp, AppColors.Neutral.`30`, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = when (variant) {
                            ToastVariant.SUCCESS -> R.drawable.ic_tick_circle
                            ToastVariant.INFO -> R.drawable.ic_info_circle
                            ToastVariant.DANGER -> R.drawable.ic_danger
                        }
                    ),
                    contentDescription = "$message icon",
                    modifier = Modifier.size(32.dp),
                    tint = when (variant) {
                        ToastVariant.SUCCESS -> AppColors.Primary.Main
                        ToastVariant.DANGER -> AppColors.Danger.Main
                        ToastVariant.INFO -> AppColors.Info.Main
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        color = messageColor
                    )
                    description?.let {
                        Text(
                            text = it,
                            color = descriptionColor,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}

private fun getToastColorSet(variant: ToastVariant): Triple<Color, Color, Color> {
    return when (variant) {
        ToastVariant.SUCCESS -> Triple(
            Color(0xFFEFFCFC),
            AppColors.Primary.`500`,
            AppColors.Primary.`500`
        )

        ToastVariant.DANGER -> Triple(
            Color(0xFFFDF3F1),
            AppColors.Danger.`100`,
            AppColors.Danger.`90`
        )

        ToastVariant.INFO -> Triple(
            Color(0xFFEAF3FC),
            AppColors.Info.`100`,
            AppColors.Info.`90`
        )
    }
}
