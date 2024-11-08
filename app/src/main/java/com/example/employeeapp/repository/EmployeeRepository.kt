package com.example.employeeapp.repository

import com.example.employeeapp.api.ApiService
import com.example.employeeapp.model.EmployeesResponse
import javax.inject.Inject

class EmployeeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getUsers(results: Int): EmployeesResponse? {
        return try {
            apiService.getUsers(results)
        } catch (e: Exception) {
            throw Exception("Failed to fetch data", e)
        }
    }
}