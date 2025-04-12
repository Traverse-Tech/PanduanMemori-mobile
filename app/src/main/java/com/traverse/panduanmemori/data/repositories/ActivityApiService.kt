package com.traverse.panduanmemori.data.repositories

import com.traverse.panduanmemori.data.dataclasses.GetActivitiesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityApiService {
    companion object {
        const val BASE_ACTIVITY_URL = "activity/"
    }

    @GET("${BASE_ACTIVITY_URL}")
    suspend fun getActivities(@Query("patientId") patientId: String, @Query("startDate") startDate: String, @Query("endDate") endDate: String): GetActivitiesResponse
}