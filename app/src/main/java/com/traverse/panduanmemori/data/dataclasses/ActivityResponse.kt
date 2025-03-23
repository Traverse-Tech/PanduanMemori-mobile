package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ActivityResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("activityCategoryId") val activityCategoryId: String,
    @field:SerializedName("patientId") val patientId: String,
    @field:SerializedName("datetime") val datetime: LocalDateTime
)