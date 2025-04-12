package com.traverse.panduanmemori.ui.task.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Note
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.elements.*
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.ui.task.TaskViewModel
import com.traverse.panduanmemori.utils.AssetUtil
import com.traverse.panduanmemori.utils.StringUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CaregiverTaskPage(taskViewModel: TaskViewModel, authViewModel: AuthViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val initState by taskViewModel.initState.collectAsState()
    val caregiverPatients by taskViewModel.caregiverPatients.collectAsState()
    val patientActivities by taskViewModel.patientActivities.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val today = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }
    var date by remember { mutableStateOf(today) }
    val checkboxStates = remember { mutableStateMapOf<String, Boolean>() }
    
    var startTimeHour by remember { mutableStateOf("") }
    var startTimeMinute by remember { mutableStateOf("") }
    var endTimeHour by remember { mutableStateOf("") }
    var endTimeMinute by remember { mutableStateOf("") }
    var selectedActivityId by remember { mutableStateOf("") }

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        taskViewModel.getInitPageData(date)
    }

    AppToast(
        message = toastMessage ?: "",
        description = toastDescription,
        variant = ToastVariant.SUCCESS,
        isVisible = toastVisible,
        onDismiss = { toastVisible = false }
    )

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                    AppTag(
                        text = "Apabila waktu mulai sudah tidak sesuai,  diganti waktunya untuk pencatatan",
                        icon = Icons.Default.Info,
                        type = TagType.OUTLINED
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Waktu Mulai",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                            color = AppColors.Neutral.`70`
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTextInput(
                                value = startTimeHour,
                                onValueChange = { startTimeHour = it },
                                placeholder = "00",
                                type = KeyboardType.Number,
                                columnModifier = Modifier.width(50.dp)
                            )

                            Text(
                                text = ":",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                color = AppColors.Neutral.`70`
                            )

                            AppTextInput(
                                value = startTimeMinute,
                                onValueChange = { startTimeMinute = it },
                                placeholder = "00",
                                type = KeyboardType.Number,
                                columnModifier = Modifier.width(50.dp)
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Waktu Selesai",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                            color = AppColors.Neutral.`70`
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTextInput(
                                value = endTimeHour,
                                onValueChange = { endTimeHour = it },
                                placeholder = "00",
                                type = KeyboardType.Number,
                                columnModifier = Modifier.width(50.dp)
                            )

                            Text(
                                text = ":",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                color = AppColors.Neutral.`70`
                            )

                            AppTextInput(
                                value = endTimeMinute,
                                onValueChange = { endTimeMinute = it },
                                placeholder = "00",
                                type = KeyboardType.Number,
                                columnModifier = Modifier.width(50.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    AppButton(
                        text = "Konfirmasi",
                        onClick = {
                            checkboxStates[selectedActivityId] = true
                            coroutineScope.launch {bottomSheetState.hide() }
                            startTimeHour = ""
                            startTimeMinute = ""
                            endTimeHour = ""
                            endTimeMinute = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        disabled = startTimeHour == "" || startTimeMinute == "" || endTimeHour == "" || endTimeMinute == ""
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (initState != ApiState.Success) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .background(Color.Gray, CircleShape)
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val initialPatientName = caregiverPatients[0].name?.first().toString().uppercase()
                                    val randomColor = initialPatientName.let {
                                        StringUtil.getRandomColorFromString(
                                            it
                                        )
                                    }
                                    val textColor = if ((randomColor.red ?: 0f) + (randomColor.green
                                            ?: 0f) + randomColor.blue > 1.5f) AppColors.Neutral.`110` else AppColors.Neutral.`10`

                                    randomColor.let {
                                        Modifier
                                            .size(40.dp)
                                            .background(color = it, shape = CircleShape)
                                    }.let {
                                        Box(
                                            modifier = it,
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = initialPatientName,
                                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                                color = textColor
                                            )
                                        }
                                    }

                                    Column {
                                        caregiverPatients[0].name?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.h2
                                            )
                                        }

                                        caregiverPatients[0].age?.let {
                                            Text(
                                                text = "$it tahun"
                                            )
                                        }
                                    }
                                }

                                AppButton(
                                    text = "Ganti Pasien",
                                    onClick = { /*TODO*/ },
                                    type = ButtonType.PRIMARY,
                                    size = ButtonSize.SMALL
                                )
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(Color(0xFFF7F3FA))
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        AppDateInput(
                            value = date,
                            onValueChange = { date = it },
                            iconColor = AppColors.Secondary.`200`,
                            title = "Tanggal Kegiatan"
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(Color(0xFFF7F3FA))
                    )
                }

                if (initState == ApiState.Success) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                                val formattedDate = remember(date) {
                                    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                                    try {
                                        val parsedDate = inputFormat.parse(date)
                                        outputFormat.format(parsedDate!!)
                                    } catch (e: Exception) {
                                        date
                                    }
                                }

                                Text(
                                    text = "Kegiatan $formattedDate",
                                    style = MaterialTheme.typography.subtitle1
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp)
                                            .background(Color(0x80EBDDF2))
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Note,
                                                contentDescription = "Sticky Note",
                                                tint = AppColors.Secondary.Main
                                            )
                                            Text(
                                                text = "${patientActivities.size} Kegiatan",
                                                style = MaterialTheme.typography.body1,
                                                color = AppColors.Neutral.`80`
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    itemsIndexed(patientActivities) { _, activity ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .border(1.dp, AppColors.Secondary.`200`, RoundedCornerShape(8.dp))
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = AssetUtil.getAssetUrl("${activity.activityCategoryIcon}.png")),
                                            contentDescription = activity.activityCategoryIcon,
                                            modifier = Modifier
                                                .size(20.dp)
                                        )
                                        activity.title?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.subtitle1
                                            )
                                        }
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = activity.occurrences?.get(0)?.datetime?.let { convertToHourMinute(it) } ?: "-",
                                            style = MaterialTheme.typography.body2,
                                        )

                                        if (activity.occurrences?.get(0)?.isOnTime == false) {
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        AppColors.Orange.Main,
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(4.dp)
                                            ) {
                                                Text(
                                                    text = "Terlewat",
                                                    style = MaterialTheme.typography.caption,
                                                    color = AppColors.Neutral.`10`
                                                )
                                            }
                                        }
                                    }
                                }

                                val isChecked = checkboxStates[activity.id] ?: false

                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {
                                                      selectedActivityId = activity.id.toString()
                                        if (!isChecked) coroutineScope.launch { bottomSheetState.show() }
                                    },
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {

                },
                backgroundColor = AppColors.Secondary.Main,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Kegiatan")
            }
        }
    }
}

fun convertToHourMinute(dateStr: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getDefault()

    val date = inputFormat.parse(dateStr)
    return outputFormat.format(date!!)
}