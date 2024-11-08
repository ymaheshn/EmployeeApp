package com.example.employeeapp.utils

import com.example.employeeapp.model.Dob
import com.example.employeeapp.model.Location
import com.example.employeeapp.model.Login
import com.example.employeeapp.model.Name
import com.example.employeeapp.model.Picture
import com.example.employeeapp.model.Street
import com.example.employeeapp.model.User

object UserUtils {

    fun createSampleUserList(): List<User> {
        val user1 = User(
            gender = "female",
            name = Name(title = "Mademoiselle", first = "Henriette", last = "Lucas"),
            location = Location(
                street = Street(number = 6633, name = "Rue Bossuet"),
                city = "Langnau im Emmental",
                state = "Valais",
                country = "Switzerland"
            ),
            email = "henriette.lucas@example.com",
            dob = Dob(date = "1991-10-13T01:20:47.452Z", age = 33),
            phone = "076 003 05 99",
            picture = Picture(
                large = "https://randomuser.me/api/portraits/women/14.jpg",
                medium = "https://randomuser.me/api/portraits/med/women/14.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/women/14.jpg"
            ),
            login = Login(uuid = "6993a67d-9ca8-4b4e-864e-56da27241040"),
            city = "Hyderabad",
            state = "",
            country = "",
            postcode = ""
        )

        val user2 = User(
            gender = "male",
            name = Name(title = "Mr", first = "Draško", last = "Vidić"),
            location = Location(
                street = Street(number = 9512, name = "Pivljanska"),
                city = "Svrljig",
                state = "Jablanica",
                country = "Serbia"
            ),
            email = "drasko.vidic@example.com",
            dob = Dob(date = "1988-12-09T03:16:42.544Z", age = 35),
            phone = "014-8083-329",
            picture = Picture(
                large = "https://randomuser.me/api/portraits/men/22.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/22.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/22.jpg"
            ),
            login = Login(uuid = "f8c53f11-b89a-45c3-9297-d34e7fad2cc5"),
            city = "Hyderabad",
            state = "",
            country = "",
            postcode = ""
        )

        return listOf(user1, user2)
    }

}