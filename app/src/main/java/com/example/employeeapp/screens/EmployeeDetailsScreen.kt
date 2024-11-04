package com.example.employeeapp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.employeeapp.viewmodels.SharedViewModel

@Composable
fun EmployeeDetailsScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val user = sharedViewModel.selectedUser

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(244.dp)
                    .clip(RectangleShape)
                    .border(2.dp, Color.Green, RectangleShape),
            ) {
                AsyncImage(
                    model = user?.picture?.large,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RectangleShape)
                )
            }
            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "Full Name: ${user?.name?.title} ${user?.name?.first} ${user?.name?.last}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )

            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "Email: ${user?.email}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )
            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "Phone: ${user?.phone}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )

            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "Age: ${user?.dob?.age}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )
            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "Address: ${user?.location?.street?.name}, ${user?.location?.street?.number}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )
            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "State: ${user?.location?.state}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )
            Text(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                text = "Country: ${user?.location?.country}",
                color = Color.Black,
                modifier = Modifier.padding(3.dp)
            )
        }
    }
    BackHandler {
        navController.popBackStack()
    }
}

