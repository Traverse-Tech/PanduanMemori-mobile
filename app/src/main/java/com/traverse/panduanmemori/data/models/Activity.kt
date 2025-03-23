package com.traverse.panduanmemori.data.models

import java.time.LocalDateTime

data class Activity(
    val id: String,
    val activityCategoryId: String,
    val activityCategory: ActivityCategory,
    val title: String,
    val time: LocalDateTime,
    val patientId: String,
    val recurrences: List<Recurrence> = emptyList(),
    val activityOccurences: List<ActivityOccurence> = emptyList()
) 