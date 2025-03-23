package com.traverse.panduanmemori.data.repositories

import com.traverse.panduanmemori.data.dataclasses.LoginRequest
import com.traverse.panduanmemori.data.dataclasses.LoginResponse
import com.traverse.panduanmemori.data.dataclasses.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    companion object {
        const val BASE_AUTH_URL = "auth/"
    }

    @POST("${BASE_AUTH_URL}login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("${BASE_AUTH_URL}register")
    suspend fun register(@Body request: RegisterRequest): LoginResponse
}
