package com.traverse.panduanmemori.data.repositories

import com.traverse.panduanmemori.data.dataclasses.BuddyConversationResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PatientApiService {
    companion object {
        const val BASE_PATIENT_URL = "patient/"
    }

    @Multipart
    @POST("${BASE_PATIENT_URL}buddy")
    suspend fun buddyConversation(@Part audio: MultipartBody.Part): BuddyConversationResponse

}