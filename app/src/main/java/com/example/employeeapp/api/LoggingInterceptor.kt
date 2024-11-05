package com.example.employeeapp.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("Interceptor", "Request: ${request.method} ${request.url}")

        // Proceed with the request
        val response = chain.proceed(request)
        Log.d("Interceptor", "Response: ${response.code} ${response.message}")

        return response
    }
}
