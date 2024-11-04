package com.example.employeeapp

import com.example.employeeapp.model.Dob
import com.example.employeeapp.model.EmployeesResponse
import com.example.employeeapp.model.Location
import com.example.employeeapp.model.Name
import com.example.employeeapp.model.Picture
import com.example.employeeapp.model.Street
import com.example.employeeapp.model.User
import com.example.employeeapp.repository.EmployeeRepository
import com.example.employeeapp.viewmodels.HomeViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @MockK
    private lateinit var homeViewModel: HomeViewModel

    @MockK
    private lateinit var employeeRepository: EmployeeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to a test dispatcher
        homeViewModel = HomeViewModel(employeeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Clean up the dispatcher after tests
    }

    @Test
    fun `fetchUsers successfully updates employees and loading state`() = runBlocking {
        // Given
        val numberOfEmployees = 2
        val mockResponse = EmployeesResponse(
            results = listOf(
                User(
                    gender = "male",
                    name = Name("Miss", "Rhonda", "Webb"),
                    location = Location(
                        street = Street(102, "Gully"),
                        city = "Hyderabad",
                        state = "Andhra",
                        country = "India",
                    ),
                    city = "Hyderabad",
                    state = "Telangana",
                    country = "India",
                    postcode = "534265",
                    email = "mahesh@gmail.com",
                    dob = Dob(date = "05-11-93", age = 25),
                    phone = "",
                    picture = Picture(large = "", medium = "", thumbnail = "")
                ),
                User(
                    gender = "female",
                    name = Name("Miss", "Rhonda", "Webb"),
                    location = Location(
                        street = Street(102, "Gully"),
                        city = "Hyderabad",
                        state = "Andhra",
                        country = "India",
                    ),
                    city = "Hyderabad",
                    state = "TG",
                    country = "India",
                    postcode = "534265",
                    email = "mahesh@gmail.com",
                    dob = Dob(date = "05-11-93", age = 25),
                    phone = "",
                    picture = Picture(large = "", medium = "", thumbnail = "")
                )
            ),
        )


        coEvery { employeeRepository.getUsers(numberOfEmployees) } returns mockResponse

        // When
        homeViewModel.fetchUsers(numberOfEmployees)

        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(homeViewModel.isLoading.value) // Loading should be true initially
        val users = mutableListOf<List<User>>()
        launch {
            homeViewModel.employees.take(1).collect { users.add(it) }
        }
        // Wait for loading to finish
        while (homeViewModel.isLoading.value) {
            // wait
            //  delay(3000)
        }
        assertTrue(users.isNotEmpty()) // Employees list should not be empty
        assertEquals(
            mockResponse.results?.size,
            users[0].size
        ) // Should match the mock response size
        assertTrue(homeViewModel.error.value == null) // Error should be null
    }

    @Test
    fun `fetchUsers handles error and updates loading state`() = runBlocking {

        val numberOfEmployees = 2

        // Given
        val errorMessage = "Network error"
        coEvery { employeeRepository.getUsers(numberOfEmployees) } throws Exception(errorMessage)

        // When
        homeViewModel.fetchUsers(numberOfEmployees)

        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(homeViewModel.isLoading.value) // Loading should be true initially
        launch {
            homeViewModel.employees.take(1).collect { }
        }
        // Wait for loading to finish
        while (homeViewModel.isLoading.value) {
            // wait
        }
        assertEquals(
            emptyList<User>(),
            homeViewModel.employees.value
        ) // Employees list should be empty
        assertEquals(
            errorMessage,
            homeViewModel.error.value
        ) // Error should match the thrown error message
    }

    @Test
    fun `fetchUsers sets loading state to false after operation`() = runBlocking {

        val numberOfEmployees = 2
        // Given
        val mockResponse = EmployeesResponse(
            results = listOf(
                User(
                    gender = "male",
                    name = Name("Miss", "Rhonda", "Webb"),
                    location = Location(
                        street = Street(102, "Gully"),
                        city = "Hyderabad",
                        state = "Andhra",
                        country = "India",
                    ),
                    city = "",
                    state = "",
                    country = "India",
                    postcode = "534265",
                    email = "mahesh@gmail.com",
                    dob = Dob(date = "05-11-93", age = 25),
                    phone = "",
                    picture = Picture(large = "", medium = "", thumbnail = "")
                ),
                User(
                    gender = "female",
                    name = Name("Miss", "Rhonda", "Webb"),
                    location = Location(
                        street = Street(102, "Gully"),
                        city = "Hyderabad",
                        state = "Andhra",
                        country = "India",
                    ),
                    city = "",
                    state = "",
                    country = "India",
                    postcode = "534265",
                    email = "mahesh@gmail.com",
                    dob = Dob(date = "05-11-93", age = 25),
                    phone = "",
                    picture = Picture(large = "", medium = "", thumbnail = "")
                )
            ),
        )

        coEvery { employeeRepository.getUsers(numberOfEmployees) } returns mockResponse

        // When
        homeViewModel.fetchUsers(numberOfEmployees)

        // Wait for loading to finish
        while (homeViewModel.isLoading.value) {
            // wait
        }

        // Then
        assertFalse(homeViewModel.isLoading.value) // Loading should be false after fetch
    }
}
