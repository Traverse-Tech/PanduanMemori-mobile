package com.traverse.panduanmemori.data.repositories

import com.traverse.panduanmemori.data.dataclasses.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface CaregiverApiService {
    companion object {
        const val BASE_CAREGIVER_URL = "caregiver/"
    }

    @POST("${BASE_CAREGIVER_URL}patients/search-by-credential")
    suspend fun searchPatientByCredential(@Body request: SearchPatientByCredentialRequest): SearchPatientByCredentialResponse

    @PATCH("${BASE_CAREGIVER_URL}updateAssignedPatient")
    suspend fun updateAssignedPatient(@Body request: UpdateAssignedPatientRequest): Response<Void>

    @GET("${BASE_CAREGIVER_URL}patients")
    suspend fun getCaregiverPatients(): GetCaregiverPatientsResponse
}
