package com.traverse.panduanmemori.data.repositories

import com.traverse.panduanmemori.data.dataclasses.GetDurationResponse
import com.traverse.panduanmemori.data.dataclasses.GetKesesuaianJadwalResponse
import retrofit2.http.GET

interface DashboardApiService {
    companion object {
        const val BASE_DASHBOARD_URL = "stats/"
    }

    @GET("${BASE_DASHBOARD_URL}get-duration/all")
    suspend fun getDuration(): GetDurationResponse

    @GET("${BASE_DASHBOARD_URL}get-kesesuaian-jadwal/all")
    suspend fun getKesesuaianJadwal(): GetKesesuaianJadwalResponse
}