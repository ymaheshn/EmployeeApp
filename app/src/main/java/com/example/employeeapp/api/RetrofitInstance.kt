package com.example.employeeapp.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java


object RetrofitInstance {

    private const val BASE_URL = "https://randomuser.me/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

}