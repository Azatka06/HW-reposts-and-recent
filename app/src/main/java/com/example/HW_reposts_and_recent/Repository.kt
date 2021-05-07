package com.example.HW_reposts_and_recent

import com.example.HW_reposts_and_recent.api.API
import com.example.HW_reposts_and_recent.api.AuthRequestParams
import com.example.HW_reposts_and_recent.api.RegistrationRequestParams
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Repository {
    // Ленивое создание Retrofit экземпляра
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://netology-back-end-post-hw-8.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                    .readTimeout(1, TimeUnit.MINUTES) // read timeout
                    .build()
            )
            .build()
    }

    // Ленивое создание API
    private val API: API by lazy {
        retrofit.create(
            com.example.HW_reposts_and_recent.api.API::class.java
        )
    }

    suspend fun authenticate(login: String, password: String) =
        API.authenticate(
            AuthRequestParams(login, password)
        )

    suspend fun registration(login: String, password: String) =
        API.register(
            RegistrationRequestParams(login, password)
        )
}