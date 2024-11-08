package com.example.employeeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.employeeapp.screens.EmployeeDetailsScreen
import com.example.employeeapp.screens.HomeScreen
import com.example.employeeapp.ui.theme.EmployeeAppTheme
import com.example.employeeapp.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()

        }
    }
}

@Composable
fun MyApp() {

    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    EmployeeAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = "homescreen"
            ) {
                composable("homescreen") {
                    HomeScreen(navController, sharedViewModel)
                }
                composable("detail") { backStackEntry ->
                    EmployeeDetailsScreen(navController, sharedViewModel)
                }
            }
        }
    }

}
