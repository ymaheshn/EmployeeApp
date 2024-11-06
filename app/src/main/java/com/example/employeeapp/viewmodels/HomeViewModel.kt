package com.example.employeeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.employeeapp.api.NetworkResult
import com.example.employeeapp.model.User
import com.example.employeeapp.repository.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val employeeRepository: EmployeeRepository) : ViewModel() {

    private val _employees = MutableStateFlow<NetworkResult<List<User>>>(NetworkResult.Loading)
    val employees: StateFlow<NetworkResult<List<User>>> = _employees.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchUsers(results: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _employees.value = NetworkResult.Loading  // Initially set to loading state
            try {
                // Make the network call
                val employeeResponse = employeeRepository.getUsers(results)

                // Check if the employeeResponse is valid and non-empty
                if (employeeResponse?.results?.isNotEmpty() == true) {
                    _employees.value = NetworkResult.Success(employeeResponse.results)
                } else {
                    _employees.value = NetworkResult.Error(Exception("No data available"))
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}


class UserViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}