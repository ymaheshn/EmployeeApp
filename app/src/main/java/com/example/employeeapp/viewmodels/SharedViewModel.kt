package com.example.employeeapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.employeeapp.model.User

class SharedViewModel : ViewModel() {
    var selectedUser: User? = null
}