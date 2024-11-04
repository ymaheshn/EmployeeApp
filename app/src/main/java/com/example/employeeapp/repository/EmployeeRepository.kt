package com.example.employeeapp.repository

import com.example.employeeapp.api.ApiService
import com.example.employeeapp.model.EmployeesResponse

class EmployeeRepository(private val apiService: ApiService) {
    suspend fun getUsers(results: Int): EmployeesResponse? {
        return apiService.getUsers(results)
    }
}