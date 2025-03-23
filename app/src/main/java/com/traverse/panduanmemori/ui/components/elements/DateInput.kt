package com.traverse.panduanmemori.ui.components.elements

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.traverse.panduanmemori.ui.components.themes.AppColors
import java.text.SimpleDateFormat
import java.util.*


// CONSTANTS
enum class DateInputType {
    DEFAULT, BIRTHDATE
}


@Composable
fun AppDateInput(
    value: String,
    onValueChange: (String) -> Unit,
    title: String = "",
    placeholder: String = "",
    modifier: Modifier = Modifier,
    iconColor: Color,
    type: DateInputType = DateInputType.DEFAULT
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                onValueChange(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        if (type == DateInputType.BIRTHDATE)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = AppColors.Neutral.`70`
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker()
                }
                .border(1.dp, AppColors.Neutral.`50`, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .background(AppColors.Neutral.`10`, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "$title date input icon",
                        modifier = Modifier.size(24.dp),
                        tint = iconColor
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.body1,
                            color = AppColors.Neutral.`50`
                        )
                    } else {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.body1.copy(color = AppColors.Neutral.`110`)
                        )
                    }
                }
            }
        }
    }
}
