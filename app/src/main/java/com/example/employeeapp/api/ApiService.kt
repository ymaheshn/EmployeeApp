package com.example.employeeapp.api
import com.example.employeeapp.model.EmployeesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getUsers(@Query("results") result: Int): EmployeesResponse?
}