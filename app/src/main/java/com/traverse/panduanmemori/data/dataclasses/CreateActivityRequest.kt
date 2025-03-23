package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CreateActivityRequest(
    @SerializedName("title") val title: String,
    @SerializedName("activityCategoryId") val activityCategoryId: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("recurrences") val recurrences: List<Recurrence>? = null
)

// Recurrence Data Class
data class Recurrence(
    @SerializedName("type") val type: String,
    @SerializedName("interval") val interval: Int? = null,
    @SerializedName("endDate") val endDate: String? = null,
    @SerializedName("weekDays") val weekDays: List<Int>? = null
)