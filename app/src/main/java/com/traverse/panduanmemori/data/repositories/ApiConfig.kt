package com.traverse.panduanmemori.data.repositories

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.traverse.panduanmemori.BuildConfig
import com.traverse.panduanmemori.data.contexts.UserContext
import com.traverse.panduanmemori.data.contexts.sessionDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class ApiConfig(context: Context) {
    private val BASE_URL = BuildConfig.BASE_URL
    private val userContext = UserContext.getInstance(context.sessionDataStore)

    private val authInterceptor = Interceptor { chain ->
        val token = runBlocking {
            userContext.getSession()
                .firstOrNull()?.token ?: ""
        }
        val original: Request = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Content-Type", "application/json")

        if (token.isNotEmpty()) {
            requestBuilder.header(
                "Authorization",
                "${BuildConfig.ACCESS_TOKEN_PREFIX} $token"
            )
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun getAuthApiService(): AuthApiService = retrofit.create(AuthApiService::class.java)
}
