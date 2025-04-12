package com.traverse.panduanmemori.ui.components.elements

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.ui.home.HomeActivity
import com.traverse.panduanmemori.ui.location.LocationActivity
import com.traverse.panduanmemori.ui.task.TaskActivity
import kotlinx.coroutines.launch

@Composable
fun AppBottomBar(
    context: Context,
    role: UserRole,
    buddyOnPress: (() -> Unit)? = null,
    isBuddyLoading: Boolean = false
) {
    val currentActivity = context as? Activity

    var isHolding by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor by animateColorAsState(
        targetValue = if (isHolding && buddyOnPress != null && !isBuddyLoading) AppColors.Primary.Main else AppColors.Neutral.`10`
    )

    val scale by animateFloatAsState(
        targetValue = if (isClicked && !isBuddyLoading) 0.9f else 1f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .background(AppColors.Neutral.`10`, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if ((role == UserRole.CAREGIVER && currentActivity?.javaClass?.simpleName == "HomeActivity") || (role == UserRole.PATIENT && currentActivity?.javaClass?.simpleName == "TaskActivity")) AppColors.Neutral.`20` else AppColors.Neutral.`10`)
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (role == UserRole.PATIENT) {

                        } else {
                            val intent = Intent(context, HomeActivity::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.overridePendingTransition(0, 0)
                        }
                    }
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (role == UserRole.PATIENT) Icons.Default.Task else Icons.Default.PieChart,
                        contentDescription = if (role == UserRole.PATIENT) "Kegiatan" else "Dashboard",
                        modifier = Modifier.size(30.dp),
                        tint = if (role == UserRole.PATIENT) AppColors.Neutral.Main else AppColors.Secondary.Main
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = if (role == UserRole.PATIENT) "Kegiatan" else "Dashboard",
                        color = if (role == UserRole.PATIENT) AppColors.Neutral.Main else AppColors.Secondary.Main,
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            if (role == UserRole.PATIENT) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .background(backgroundColor, CircleShape)
                        .scale(scale)
                        .let {
                            if (isBuddyLoading) { it }
                            else if (buddyOnPress != null) {
                                it.pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isHolding = true
                                            coroutineScope.launch {
                                                buddyOnPress.invoke()
                                            }
                                            tryAwaitRelease()
                                            isHolding = false
                                            coroutineScope.launch {
                                                buddyOnPress.invoke()
                                            }
                                        }
                                    )
                                }
                            } else {
                                it.clickable {
                                    val intent = Intent(context, HomeActivity::class.java)
                                    context.startActivity(intent)
                                    (context as? Activity)?.overridePendingTransition(0, 0)
                                }
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isBuddyLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Bantuan",
                                modifier = Modifier.size(30.dp),
                                tint = if (isHolding) AppColors.Neutral.`10` else AppColors.Neutral.Main
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Bantuan",
                                color = if (isHolding) AppColors.Neutral.`10` else AppColors.Neutral.Main,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if ((role == UserRole.CAREGIVER && currentActivity?.javaClass?.simpleName == "TaskActivity") || (role == UserRole.PATIENT && currentActivity?.javaClass?.simpleName == "LocationActivity")) AppColors.Neutral.`20` else AppColors.Neutral.`10`)
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (role == UserRole.PATIENT) {
                            val intent = Intent(context, LocationActivity::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.overridePendingTransition(0, 0)
                        } else {
                            val intent = Intent(context, TaskActivity::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.overridePendingTransition(0, 0)
                        }
                    }
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (role == UserRole.PATIENT) Icons.Default.LocationOn else Icons.Default.Task,
                        contentDescription = if (role == UserRole.PATIENT) "Lokasi" else "Kegiatan",
                        modifier = Modifier.size(30.dp),
                        tint = if (role == UserRole.PATIENT) AppColors.Neutral.Main else AppColors.Secondary.Main
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = if (role == UserRole.PATIENT) "Lokasi" else "Kegiatan",
                        color = if (role == UserRole.PATIENT) AppColors.Neutral.Main else AppColors.Secondary.Main,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
