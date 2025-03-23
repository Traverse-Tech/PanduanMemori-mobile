package com.traverse.panduanmemori.data.dataclasses
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GetActivitiesInRangeRequest(
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String
)