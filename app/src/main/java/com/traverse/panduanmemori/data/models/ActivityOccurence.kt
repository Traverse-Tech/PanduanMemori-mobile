package com.traverse.panduanmemori.data.models

import java.time.LocalDateTime

data class ActivityOccurence(
    val id: String,
    val activityId: String,
    val activity: Activity,
    val recurrenceId: String?,
    val recurrence: Recurrence?,
    val datetime: LocalDateTime,
    val isCompleted: Boolean = false
) 