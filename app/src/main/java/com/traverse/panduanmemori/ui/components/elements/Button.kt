package com.traverse.panduanmemori.ui.components.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.traverse.panduanmemori.ui.components.themes.AppColors

// CONSTANTS
enum class ButtonVariant {
    PRIMARY, SECONDARY, TERTIARY, TEXT_ONLY
}

enum class ButtonType {
    PRIMARY, ORANGE, CYAN, DANGER
}

enum class ButtonSize(val horizontalPadding: Int, val verticalPadding: Int, val gap: Int) {
    SMALL(horizontalPadding = 16, verticalPadding = 8, gap = 8),
    MEDIUM(horizontalPadding = 20, verticalPadding = 12, gap = 8),
    LARGE(horizontalPadding = 24, verticalPadding = 14, gap = 8)
}

enum class IconPosition {
    LEFT, RIGHT, ONLY
}


// COMPONENTS
@Composable
fun AppButton(
    text: String? = "",
    onClick: () -> Unit,
    disabled: Boolean = false,
    size: ButtonSize = ButtonSize.MEDIUM,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    type: ButtonType = ButtonType.PRIMARY,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.LEFT
) {
    val buttonColors = buttonColors(disabled, variant, type)
    val buttonSize = when (size) {
        ButtonSize.LARGE -> 64.dp
        ButtonSize.MEDIUM -> 56.dp
        else -> 48.dp
    }
    val iconSize = if (iconPosition == IconPosition.ONLY) buttonSize * 0.5f else 24.dp

    androidx.compose.material.Button(
        onClick = onClick,
        shape = RoundedCornerShape(200.dp),
        modifier = if (iconPosition == IconPosition.ONLY) {
            Modifier.size(buttonSize)
        } else {
            Modifier
        },
        colors = buttonColors,
        enabled = !disabled,
        border = if (variant == ButtonVariant.SECONDARY && !disabled) {
            BorderStroke(2.dp, buttonColors.contentColor(enabled = true).value)
        } else {
            null
        },
        contentPadding = if (iconPosition != IconPosition.ONLY) {
            PaddingValues(
                vertical = size.verticalPadding.dp,
                horizontal = size.horizontalPadding.dp
            )
        } else { PaddingValues(0.dp) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null && iconPosition == IconPosition.LEFT) {
                IconWrapper(icon, iconSize)
                if (!text.isNullOrEmpty()) Spacer(modifier = Modifier.width(8.dp))
            }

            if (!text.isNullOrEmpty() && iconPosition != IconPosition.ONLY) {
                Text(text = text)
            }

            if (icon != null && iconPosition == IconPosition.ONLY) {
                IconWrapper(icon, iconSize)
            }

            if (icon != null && iconPosition == IconPosition.RIGHT) {
                if (!text.isNullOrEmpty()) Spacer(modifier = Modifier.width(8.dp))
                IconWrapper(icon, iconSize)
            }
        }
    }
}

@Composable
private fun IconWrapper(icon: ImageVector, size: Dp) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(size)
        )
    }
}

@Composable
fun buttonColors(
    disabled: Boolean,
    variant: ButtonVariant,
    type: ButtonType = ButtonType.PRIMARY
) = when (type) {
    ButtonType.PRIMARY -> when (variant) {
        ButtonVariant.PRIMARY -> if (disabled)  {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`50`,
                contentColor = AppColors.Neutral.`10`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Secondary.Main,
                contentColor = AppColors.Neutral.`10`
            )
        }

        ButtonVariant.SECONDARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor =  AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`10`,
                contentColor = AppColors.Secondary.Main
            )
        }

        ButtonVariant.TERTIARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor =  AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Secondary.`100`,
                contentColor = AppColors.Secondary.Main
            )
        }

        ButtonVariant.TEXT_ONLY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Secondary.Main
            )
        }
    }

    ButtonType.ORANGE -> when (variant) {
        ButtonVariant.PRIMARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`50`,
                contentColor = AppColors.Neutral.`10`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Orange.Main,
                contentColor = AppColors.Neutral.`10`
            )
        }

        ButtonVariant.SECONDARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`10`,
                contentColor = AppColors.Orange.Main
            )
        }

        ButtonVariant.TERTIARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Orange.`10`,
                contentColor = AppColors.Orange.Main
            )
        }

        ButtonVariant.TEXT_ONLY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Orange.Main
            )
        }
    }

    ButtonType.CYAN -> when (variant) {
        ButtonVariant.PRIMARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`50`,
                contentColor = AppColors.Neutral.`10`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Primary.Main,
                contentColor = AppColors.Neutral.`10`
            )
        }

        ButtonVariant.SECONDARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`10`,
                contentColor = AppColors.Primary.Main
            )
        }

        ButtonVariant.TERTIARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Primary.`100`,
                contentColor = AppColors.Primary.Main
            )
        }

        ButtonVariant.TEXT_ONLY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Primary.Main
            )
        }
    }

    ButtonType.DANGER -> when (variant) {
        ButtonVariant.PRIMARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`50`,
                contentColor = AppColors.Neutral.`10`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Danger.Main,
                contentColor = AppColors.Neutral.`10`
            )
        }

        ButtonVariant.SECONDARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`10`,
                contentColor = AppColors.Danger.Main
            )
        }

        ButtonVariant.TERTIARY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Neutral.`20`,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Danger.`10`,
                contentColor = AppColors.Danger.Main
            )
        }

        ButtonVariant.TEXT_ONLY -> if (disabled) {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Neutral.`50`
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = AppColors.Transparent,
                contentColor = AppColors.Danger.Main
            )
        }
    }
}
