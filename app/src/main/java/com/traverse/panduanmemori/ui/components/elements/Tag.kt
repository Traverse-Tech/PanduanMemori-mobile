package com.traverse.panduanmemori.ui.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.traverse.panduanmemori.ui.components.themes.AppColors

// CONSTANTS
enum class TagVariant {
    PRIMARY, WARNING, DANGER, INFO, GREY
}

enum class TagType {
    FILLED, OUTLINED
}


// COMPONENTS
@Composable
fun AppTag(
    text: String,
    icon: ImageVector? = null,
    variant: TagVariant = TagVariant.PRIMARY,
    type: TagType = TagType.FILLED
) {
    val (backgroundColor, outlinedColor, contentColor) = getTagColor(variant, type)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(200.dp))
            .background(backgroundColor)
            .let {
                if (type == TagType.OUTLINED)
                    it.border(2.dp, outlinedColor, RoundedCornerShape(200.dp))
                else
                    it
            }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null)
                IconWrapper(icon = icon, color = contentColor, text = text)

            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                color = contentColor
            )
        }
    }
}

@Composable
private fun IconWrapper(icon: ImageVector, color: Color, text: String) {
    Box(
        modifier = Modifier.size(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$text tag",
            modifier = Modifier.size(14.dp),
            tint = color
        )
    }
}

private fun getTagColor(variant: TagVariant, type: TagType): Triple<Color, Color, Color> {
    return when (variant) {
        TagVariant.PRIMARY -> when(type) {
            TagType.FILLED -> Triple(AppColors.Primary.Main, AppColors.Primary.Main, AppColors.Neutral.`10`)
            TagType.OUTLINED -> Triple(AppColors.Primary.`100`, AppColors.Primary.`200`, AppColors.Primary.`500`)
        }

        TagVariant.WARNING -> when (type) {
            TagType.FILLED -> Triple(AppColors.Orange.Main, AppColors.Orange.Main, AppColors.Neutral.`10`)
            TagType.OUTLINED -> Triple(AppColors.Orange.`10`, AppColors.Orange.`30`, AppColors.Orange.`80`)
        }

        TagVariant.DANGER -> when (type) {
            TagType.FILLED -> Triple(AppColors.Danger.Main, AppColors.Danger.Main, AppColors.Neutral.`10`)
            TagType.OUTLINED -> Triple(AppColors.Danger.`10`, AppColors.Danger.`30`, AppColors.Danger.`80`)
        }

        TagVariant.INFO -> when (type) {
            TagType.FILLED -> Triple(AppColors.Info.Main, AppColors.Info.Main, AppColors.Neutral.`10`)
            TagType.OUTLINED -> Triple(AppColors.Info.`10`, AppColors.Info.`30`, AppColors.Info.`80`)
        }

        TagVariant.GREY -> when (type) {
            TagType.FILLED -> Triple(AppColors.Neutral.`30`, AppColors.Neutral.`30`, AppColors.Neutral.`90`)
            TagType.OUTLINED -> Triple(AppColors.Neutral.`30`, AppColors.Neutral.`80`, AppColors.Neutral.`80`)
        }
    }
}
