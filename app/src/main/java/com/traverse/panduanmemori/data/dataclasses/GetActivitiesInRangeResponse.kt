package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GetActivitiesInRangeResponse(
    @SerializedName("activities") val activities: List<ActivityWithOccurrences>
)