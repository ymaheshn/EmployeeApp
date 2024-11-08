package com.example.employeeapp.model

data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val email: String,
    val dob: Dob,
    val phone: String,
    val picture: Picture,
    val login: Login
)

data class Name(val title: String, val first: String, val last: String)
data class Location(val street: Street, val city: String, val state: String, val country: String)
data class Street(val number: Int, val name: String)
data class Dob(val date: String, val age: Int)
data class Picture(val large: String, val medium: String, val thumbnail: String)
data class Login(val uuid: String)
