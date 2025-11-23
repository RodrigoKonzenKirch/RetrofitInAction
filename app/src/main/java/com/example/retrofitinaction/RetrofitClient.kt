package com.example.retrofitinaction

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ====================================================================
 * Retrofit Setup
 * ====================================================================
 */
object RetrofitClient {
    // Use a non-existent Base URL. It doesn't matter since the Interceptor will stop the request.
    private const val BASE_URL = "https://mock.api.com/"

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // Custom interceptor
            .addInterceptor(MockInterceptor())
            // Add a small delay to simulate network latency for better testing feel
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}