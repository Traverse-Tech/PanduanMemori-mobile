package com.traverse.panduanmemori.data.models

import java.time.LocalDateTime

data class ActivityCategory(
    val id: String,
    val name: String,
    val icon: String,
    val activities: List<Activity> = emptyList()
) 