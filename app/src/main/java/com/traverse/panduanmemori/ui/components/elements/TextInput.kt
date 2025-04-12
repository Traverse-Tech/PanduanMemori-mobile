package com.traverse.panduanmemori.ui.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.traverse.panduanmemori.ui.components.themes.AppColors

@Composable
fun AppTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    title: String = "",
    placeholder: String = "",
    type: KeyboardType = KeyboardType.Text,
    leftIcon: ImageVector? = null,
    leftIconColor: Color = AppColors.Primary.Main,
    rightIcon: ImageVector? = null,
    rightIconColor: Color = AppColors.Primary.Main,
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier.fillMaxWidth()
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = columnModifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = AppColors.Neutral.`70`
        )

        Spacer(modifier = Modifier.height(4.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, AppColors.Neutral.`50`, RoundedCornerShape(8.dp))
                .padding(8.dp),
            textStyle = MaterialTheme.typography.body1.copy(color = AppColors.Neutral.`110`),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = type
            ),
            visualTransformation = if (type == KeyboardType.Password && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(AppColors.Neutral.`10`, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (leftIcon != null) {
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = leftIcon,
                                    contentDescription = "$title input icon 1",
                                    modifier = Modifier.size(24.dp),
                                    tint = leftIconColor
                                )
                            }
                        }

                        if (type == KeyboardType.Phone) {
                            Text(
                                text = "+62",
                                style = MaterialTheme.typography.body1,
                                color = leftIconColor
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.body1,
                                    color = AppColors.Neutral.`50`
                                )
                            }
                            innerTextField()
                        }

                        if (type == KeyboardType.Password) {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = rightIconColor
                                )
                            }
                        } else {
                            if (rightIcon != null) {
                                Box(
                                    modifier = Modifier.size(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = rightIcon,
                                        contentDescription = "$title input icon 2",
                                        modifier = Modifier.size(24.dp),
                                        tint = rightIconColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
