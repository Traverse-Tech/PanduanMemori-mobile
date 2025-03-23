package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ActivityOccurrence(
    @SerializedName("id") val id: String,
    @SerializedName("recurrenceId") val recurrenceId: String? = null,
    @SerializedName("activityId") val activityId: String,
    @SerializedName("datetime") val datetime: LocalDateTime,
    @SerializedName("isCompleted") val isCompleted: Boolean
)

data class ActivityWithOccurrences(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("activityCategoryId") val activityCategoryId: String,
    @SerializedName("patientId") val patientId: String,
    @SerializedName("occurrences") val occurrences: List<ActivityOccurrence>
)