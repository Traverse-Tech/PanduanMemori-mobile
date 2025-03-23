package com.traverse.panduanmemori.data.repositories

import com.traverse.panduanmemori.data.dataclasses.*
import retrofit2.Response
import retrofit2.http.*

interface ActivityApiService {
    companion object {
        const val BASE_ACTIVITY_URL = "activity/"
    }

    @POST("${BASE_ACTIVITY_URL}")
    suspend fun createActivity(@Body request: CreateActivityRequest): ActivityResponse

    @GET("${BASE_ACTIVITY_URL}")
    suspend fun getActivitiesInRange(
        @Body request: GetActivitiesInRangeRequest
    ): GetActivitiesInRangeResponse

    @PATCH("${BASE_ACTIVITY_URL}{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body request: UpdateActivityRequest
    ): ActivityResponse

    @DELETE("${BASE_ACTIVITY_URL}future/{id}")
    suspend fun deleteFutureActivity(@Path("id") id: String)

    @DELETE("${BASE_ACTIVITY_URL}all/{id}")
    suspend fun deleteAllActivity(@Path("id") id: String)

    @PATCH("${BASE_ACTIVITY_URL}complete/{id}")
    suspend fun completeActivityOccurence(@Path("id") id: String): ActivityOccurrenceResponse

    @PATCH("${BASE_ACTIVITY_URL}occurence/{id}")
    suspend fun updateActivityOccurence(
        @Path("id") id: String,
        @Body request: UpdateActivityRequest
    ): ActivityOccurrenceResponse
}