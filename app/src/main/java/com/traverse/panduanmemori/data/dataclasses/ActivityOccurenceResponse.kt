package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ActivityOccurrenceResponse(
    @SerializedName("id") val id: String,
    @SerializedName("activityId") val activityId: String,
    @SerializedName("datetime") val datetime: LocalDateTime,
    @SerializedName("isCompleted") val isCompleted: Boolean
)
