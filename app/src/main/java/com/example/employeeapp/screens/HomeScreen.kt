package com.example.employeeapp.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.employeeapp.api.RetrofitInstance
import com.example.employeeapp.model.User
import com.example.employeeapp.repository.EmployeeRepository
import com.example.employeeapp.viewmodels.HomeViewModel
import com.example.employeeapp.viewmodels.SharedViewModel
import com.example.employeeapp.viewmodels.UserViewModelFactory

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {

    val context = LocalContext.current
    val retrofitInstance = RetrofitInstance
    val api = retrofitInstance.api

    val employeeRepository = remember {
        EmployeeRepository(apiService = api)
    }

    val userViewModelFactory = UserViewModelFactory(employeeRepository)

    val homeViewModel: HomeViewModel = ViewModelProvider(
        context as ViewModelStoreOwner,
        userViewModelFactory
    )[HomeViewModel::class.java]

    val employees = homeViewModel.employees.collectAsState().value
    val error = homeViewModel.error.collectAsState().value
    val isLoading = homeViewModel.isLoading.collectAsState().value
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (error != null) {
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_LONG).show()
    }

    if (isLoading) {
        Box(
            modifier = Modifier.padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(2.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    } else {
        Column {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter No") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 6.dp, end = 6.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            Button(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                onClick = {
                    homeViewModel.fetchUsers(query.toIntOrNull() ?: 10)
                    keyboardController?.hide()
                }) {
                Text("Search")
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 20.dp)
            ) {
                employees.forEachIndexed { index, item ->
                    items(count = index) {
                        ItemView(item) {
                            sharedViewModel.selectedUser = item
                            navController.navigate("detail")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemView(employee: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {

            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Red, CircleShape)
                    .align(Alignment.CenterVertically)
            ) {
                AsyncImage(
                    model = employee.picture.medium,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    /*  placeholder = painterResource(id = R.drawable.ic_launcher_background),
                      error = painterResource(id = R.drawable.ic_launcher_background),*/
                )
            }
            Column(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                Text(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    text = "First Name: ${employee.name.first}",
                    color = Color.Black,
                    modifier = Modifier.padding(2.dp)
                )

                Text(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    text = "Last Name: ${employee.name.last}",
                    color = Color.Black,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    text = "Age: ${employee.dob.age}",
                    color = Color.Black,
                    modifier = Modifier.padding(2.dp)
                )
            }

        }
    }
}

