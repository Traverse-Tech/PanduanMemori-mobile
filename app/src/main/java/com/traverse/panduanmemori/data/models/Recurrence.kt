package com.traverse.panduanmemori.data.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Recurrence(
    val id: String,
    val activityId: String,
    val activity: Activity,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val interval: Int = 1,
    val type: RecurrenceType,
    val weekDays: List<Int> = emptyList(),
    val activityOccurences: List<ActivityOccurence> = emptyList()
)

enum class RecurrenceType {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}