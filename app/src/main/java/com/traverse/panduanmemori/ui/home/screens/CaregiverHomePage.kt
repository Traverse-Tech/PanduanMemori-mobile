package com.traverse.panduanmemori.ui.home.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.auth.AuthActivity
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.elements.AppButton
import com.traverse.panduanmemori.ui.components.elements.ButtonSize
import com.traverse.panduanmemori.ui.components.elements.ButtonType
import com.traverse.panduanmemori.ui.components.elements.ButtonVariant
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.ui.home.HomeViewModel
import com.traverse.panduanmemori.utils.AssetUtil
import com.traverse.panduanmemori.utils.StringUtil
import kotlinx.coroutines.launch

@Composable
fun CaregiverHomePage(context: Context, homeViewModel: HomeViewModel, authViewModel: AuthViewModel) {
    val getUser = authViewModel.getUser()
    val coroutineScope = rememberCoroutineScope()
    val initialName = getUser.name.first().toString().uppercase()
    val randomColor = StringUtil.getRandomColorFromString(initialName)
    val textColor = if (randomColor.red + randomColor.green + randomColor.blue > 1.5f) AppColors.Neutral.`110` else AppColors.Neutral.`10`
    var currentStaticState by remember { mutableStateOf("SEMUA") }
    val getKesesuaianJadwalData by homeViewModel.dashboardKesesuaianJadwalData.collectAsState()
    val getDuration by homeViewModel.dashboardDurationData.collectAsState()
    val getDashboardDataState by homeViewModel.dashboardApiState.collectAsState()
    
    var showState by remember { mutableStateOf(false) }

    coroutineScope.launch { homeViewModel.getDashboardDataPage() }
    val chartData = remember { mutableStateOf(
        mapOf(
            "Tepat Waktu" to (AppColors.Secondary.`100` to 0f),
            "Terlambat" to (AppColors.Secondary.`200` to 0f),
            "Tidak Dilakukan" to (AppColors.Secondary.Main to 0f)
        )
    ) }

    LaunchedEffect(Unit) {
        if (getDashboardDataState == ApiState.Success) {
            chartData.value = mapOf(
                "Tepat Waktu" to (AppColors.Secondary.`100` to (getKesesuaianJadwalData.semua?.tepatWaktu
                    ?: 0f)),
                "Terlambat" to (AppColors.Secondary.`200` to (getKesesuaianJadwalData.semua?.telat
                    ?: 0f)),
                "Tidak Dilakukan" to (AppColors.Secondary.Main to (getKesesuaianJadwalData.semua?.terlewat
                    ?: 0f))
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9E5ABE),
                            Color(0xFF8134A5)
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 32.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column{
                    Text(text = "Halo,", style = MaterialTheme.typography.body1, color = AppColors.Neutral.`10`)
                    Text(text = getUser.name, style = MaterialTheme.typography.body1, color = AppColors.Neutral.`10`)
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = randomColor, shape = CircleShape)
                        .clickable {
                            coroutineScope.launch {
                                authViewModel.logout()
                            }
                            val intent = Intent(context, AuthActivity::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.overridePendingTransition(0, 0)
                            (context as? Activity)?.finish()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initialName,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cek Statistik Sebulan Terakhir",
                style = MaterialTheme.typography.h2,
                color = AppColors.Neutral.`10`
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.Neutral.`10`, RoundedCornerShape(8.dp))
                    .padding(12.dp)
                    .clickable {
                        showState = !showState
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showState) "Yusuf Qudus" else "Semua Pasien",
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow down",
                        tint = Color.Black
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (showState) {
                Image(
                    painter = rememberAsyncImagePainter(model = AssetUtil.getAssetUrl("10.png")),
                    contentDescription = "Wordcloud",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .aspectRatio(1f)
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(AppColors.Secondary.`100`, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_elder),
                                        contentDescription = "Adult Icon",
                                        tint = AppColors.Secondary.Main
                                    )
                                }

                                AppButton(onClick = { /*TODO*/ }, text = "Tambah", type = ButtonType.PRIMARY, variant = ButtonVariant.SECONDARY, size = ButtonSize.SMALL)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = "Jumlah pasien yang dirawat", style = MaterialTheme.typography.caption)

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(text = "4", style = MaterialTheme.typography.h1, color = AppColors.Secondary.Main)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(AppColors.Secondary.`100`, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning Icon",
                                    tint = AppColors.Danger.Main
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = "Total percobaan keluar lokasi aman", style = MaterialTheme.typography.caption)

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(text = "2", style = MaterialTheme.typography.h1, color = AppColors.Secondary.Main)

                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Statistik Kegiatan", style = MaterialTheme.typography.h2)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (currentStaticState != "SEMUA") Color(0xFFEBDDF2) else AppColors.Secondary.`200`,
                                shape = RoundedCornerShape(200.dp)
                            )
                            .border(1.dp, AppColors.Secondary.`200`, RoundedCornerShape(200.dp))
                            .padding(vertical = 4.dp, horizontal = 20.dp)
                            .clickable {
                                currentStaticState = "SEMUA"

                                chartData.value = mapOf(
                                    "Tepat Waktu" to (AppColors.Secondary.`100` to (getKesesuaianJadwalData.semua?.tepatWaktu
                                        ?: 0f)),
                                    "Terlambat" to (AppColors.Secondary.`200` to (getKesesuaianJadwalData.semua?.telat
                                        ?: 0f)),
                                    "Tidak Dilakukan" to (AppColors.Secondary.Main to (getKesesuaianJadwalData.semua?.terlewat
                                        ?: 0f))
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Semua",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                            color = if (currentStaticState == "SEMUA") AppColors.Neutral.`10` else AppColors.Secondary.`500`
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (currentStaticState != "MAKAN") Color(0xFFEBDDF2) else AppColors.Secondary.`200`,
                                shape = RoundedCornerShape(200.dp)
                            )
                            .border(1.dp, AppColors.Secondary.`200`, RoundedCornerShape(200.dp))
                            .padding(vertical = 4.dp, horizontal = 20.dp)
                            .clickable {
                                currentStaticState = "MAKAN"

                                chartData.value = mapOf(
                                    "Tepat Waktu" to (AppColors.Secondary.`100` to (getKesesuaianJadwalData.makan?.tepatWaktu
                                        ?: 0f)),
                                    "Terlambat" to (AppColors.Secondary.`200` to (getKesesuaianJadwalData.makan?.telat
                                        ?: 0f)),
                                    "Tidak Dilakukan" to (AppColors.Secondary.Main to (getKesesuaianJadwalData.makan?.terlewat
                                        ?: 0f))
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Makan",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                            color = if (currentStaticState == "MAKAN") AppColors.Neutral.`10` else AppColors.Secondary.`500`
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (currentStaticState != "OBAT") Color(0xFFEBDDF2) else AppColors.Secondary.`200`,
                                shape = RoundedCornerShape(200.dp)
                            )
                            .border(1.dp, AppColors.Secondary.`200`, RoundedCornerShape(200.dp))
                            .padding(vertical = 4.dp, horizontal = 20.dp)
                            .clickable {
                                currentStaticState = "OBAT"

                                chartData.value = mapOf(
                                    "Tepat Waktu" to (AppColors.Secondary.`100` to (getKesesuaianJadwalData.obat?.tepatWaktu
                                        ?: 0f)),
                                    "Terlambat" to (AppColors.Secondary.`200` to (getKesesuaianJadwalData.obat?.telat
                                        ?: 0f)),
                                    "Tidak Dilakukan" to (AppColors.Secondary.Main to (getKesesuaianJadwalData.obat?.terlewat
                                        ?: 0f))
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Obat",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                            color = if (currentStaticState == "OBAT") AppColors.Neutral.`10` else AppColors.Secondary.`500`
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (currentStaticState != "TIDUR") Color(0xFFEBDDF2) else AppColors.Secondary.`200`,
                                shape = RoundedCornerShape(200.dp)
                            )
                            .border(1.dp, AppColors.Secondary.`200`, RoundedCornerShape(200.dp))
                            .padding(vertical = 4.dp, horizontal = 20.dp)
                            .clickable {
                                currentStaticState = "TIDUR"

                                chartData.value = mapOf(
                                    "Tepat Waktu" to (AppColors.Secondary.`100` to (getKesesuaianJadwalData.tidur?.tepatWaktu
                                        ?: 0f)),
                                    "Terlambat" to (AppColors.Secondary.`200` to (getKesesuaianJadwalData.tidur?.telat
                                        ?: 0f)),
                                    "Tidak Dilakukan" to (AppColors.Secondary.Main to (getKesesuaianJadwalData.tidur?.terlewat
                                        ?: 0f))
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidur",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                            color = if (currentStaticState == "TIDUR") AppColors.Neutral.`10` else AppColors.Secondary.`500`
                        )
                    }
                }

                if (currentStaticState == "MAKAN" || currentStaticState == "TIDUR") {
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEBDDF2), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = AssetUtil.getAssetUrl(if (currentStaticState == "MAKAN") "food-icon.png" else "sleep-icon.png")),
                                        contentDescription = if (currentStaticState == "MAKAN") "food icon" else "sleep-icon",
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }

                                Text(text = "Durasi Umum ${if (currentStaticState == "MAKAN") "Makan" else "Tidur"}", color = AppColors.Secondary.Main)
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = if (currentStaticState == "MAKAN") getDuration.makan.toString() else getDuration.tidur.toString(), style = MaterialTheme.typography.h2, color = AppColors.Secondary.Main)
                                Text(text = "Menit", color = AppColors.Secondary.Main)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Canvas(
                            modifier = Modifier.size(160.dp)
                        ) {
                            var startAngle = 45f
                            chartData.value.values.forEach { (color, percentage) ->
                                val sweepAngle = 360 * percentage / 100
                                drawArc(
                                    color = color,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = true
                                )
                                startAngle += sweepAngle
                            }
                        }

                        // Legend / Penjelasan warna
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Kesesuaian Jadwal",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )

                            chartData.value.forEach { (key, value) ->
                                val (color, _) = value
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(color, shape = RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = key, style = MaterialTheme.typography.body2)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}