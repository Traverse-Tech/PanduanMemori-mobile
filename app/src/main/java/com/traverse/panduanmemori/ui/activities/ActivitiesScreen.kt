//package com.traverse.panduanmemori.ui.activities
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.traverse.panduanmemori.data.dataclasses.*
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.time.format.TextStyle
//import java.util.*
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ActivitiesScreen(
//    viewModel: ActivitiesViewModel
//) {
//    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
//    var showDatePicker by remember { mutableStateOf(false) }
//    val activities by viewModel.activities.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // Date Picker Card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(140.dp),
//            elevation = 4.dp
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Button(onClick = { showDatePicker = true }) {
//                    Text("${selectedDate.month.getDisplayName(TextStyle.FULL, Locale("id"))} ${selectedDate.year}")
//                }
//
//                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    val days = (-3..3).map { selectedDate.plusDays(it.toLong()) }
//                    items(days) { date ->
//                        DayItem(
//                            date = date,
//                            isSelected = date == selectedDate,
//                            onClick = { selectedDate = date }
//                        )
//                    }
//                }
//            }
//        }
//
//        // Activities List
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            elevation = 4.dp
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Text(
//                    text = "Kegiatan ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id")))}",
//                    fontSize = 18.sp
//                )
//                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    items(activities) { activity ->
//                        ActivityItem(activity.occurrences)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun DayItem(
//    date: LocalDate,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .width(48.dp)
//            .height(55.dp)
//            .border(
//                width = 1.dp,
//                color = if (isSelected) Color.Blue else Color.Gray,
//                shape = RoundedCornerShape(8.dp)
//            )
//            .background(
//                color = if (isSelected) Color.Blue else Color.LightGray,
//                shape = RoundedCornerShape(8.dp)
//            )
//            .padding(8.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("id")), fontSize = 12.sp)
//            Text(text = date.dayOfMonth.toString(), fontSize = 14.sp)
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ActivityItem(activityOccurrence: ActivityOccurrence) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(70.dp)
//            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
//        elevation = 2.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp, vertical = 12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(text = activityOccurrence.activityId, fontSize = 14.sp)
//                Text(text = activityOccurrence.datetime.format(DateTimeFormatter.ofPattern("HH:mm")), fontSize = 12.sp)
//            }
//            Checkbox(
//                checked = activityOccurrence.isCompleted,
//                onCheckedChange = { /* Handle completion */ }
//            )
//        }
//    }
//}
package com.traverse.panduanmemori.ui.activities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.traverse.panduanmemori.data.dataclasses.ActivityOccurrence
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle as ts
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivitiesScreen(
    viewModel: ActivitiesViewModel
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val activities by viewModel.activities.collectAsState()
    val activitiesState by viewModel.activitiesState.collectAsState()

    LaunchedEffect(selectedDate) {
        viewModel.loadActivities(selectedDate, selectedDate.plusDays(6))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Date Picker Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { showDatePicker = true }) {
                    Text(
                        text = "${selectedDate.month.getDisplayName(ts.FULL, Locale("id"))} ${selectedDate.year}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val days = (-3..3).map { selectedDate.plusDays(it.toLong()) }
                    items(days) { date ->
                        DayItem(
                            date = date,
                            isSelected = date == selectedDate,
                            onClick = { selectedDate = date }
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Kegiatan ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id")))}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF262626)
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "12 Kegiatan",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF616161)
                        )
                    )
                    Text(
                        text = "2 Terlewat",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF616161)
                        )
                    )
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(activities) { activity ->
                        ActivityItem(
                            activityOccurrence = activity,
                            onComplete = { viewModel.completeActivity(activity.id) }
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Pilih Bulan dan Tahun") },
            text = {
                // TODO: date picker
            },
            confirmButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(55.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF8134A5) else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) Color(0xFF8134A5) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfWeek.getDisplayName(ts.SHORT, Locale("id")),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color(0xFF9E5ABE)
                )
            )
            Text(
                text = date.dayOfMonth.toString(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color(0xFF9E5ABE)
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityItem(
    activityOccurrence: ActivityOccurrence,
    onComplete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF9E5ABE),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = activityOccurrence.activityId,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Text(
                    text = activityOccurrence.datetime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            }
            Checkbox(
                checked = activityOccurrence.isCompleted,
                onCheckedChange = { onComplete() }
            )
        }
    }
}