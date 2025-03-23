package com.traverse.panduanmemori.data.dataclasses
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UpdateActivityOccurrenceRequest(
    @SerializedName("title") val title: String? = null,
    @SerializedName("activityCategoryId") val activityCategoryId: String? = null,
    @SerializedName("datetime") val datetime: String? = null
)