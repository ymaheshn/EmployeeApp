import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.employeeapp.api.NetworkResult
import com.example.employeeapp.model.Dob
import com.example.employeeapp.model.EmployeesResponse
import com.example.employeeapp.model.Location
import com.example.employeeapp.model.Login
import com.example.employeeapp.model.Name
import com.example.employeeapp.model.Picture
import com.example.employeeapp.model.Street
import com.example.employeeapp.model.User
import com.example.employeeapp.repository.EmployeeRepository
import com.example.employeeapp.utils.UserUtils
import com.example.employeeapp.viewmodels.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Rule to allow live data to be observed synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // The ViewModel under test
    private lateinit var homeViewModel: HomeViewModel

    // Mock the EmployeeRepository
    private val employeeRepository: EmployeeRepository = mockk()

    // Dispatcher for testing coroutines
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Create ViewModel instance with mocked repository
        homeViewModel = HomeViewModel(employeeRepository)
    }

    @Test
    fun `fetchUsers should update employees with success result`() = runTest {
        // Given

        val mockUserList = listOf(
            User(
                gender = "male",
                name = Name("Miss", "Rhonda", "Webb"),
                location = Location(
                    street = Street(102, "Gully"),
                    city = "Hyderabad",
                    state = "Andhra",
                    country = "India"
                ),
                city = "Hyderabad",
                state = "Telangana",
                country = "India",
                postcode = "534265",
                email = "mahesh@gmail.com",
                dob = Dob(date = "05-11-93", age = 25),
                phone = "",
                picture = Picture(large = "", medium = "", thumbnail = ""),
                login = Login("")
            )
        )

        val employeesResponse = EmployeesResponse(UserUtils.createSampleUserList())

        // Mock repository response
        coEvery { employeeRepository.getUsers(2) } returns employeesResponse


        // When
        homeViewModel.fetchUsers(2)

        // Advance the dispatcher to ensure the coroutines are executed
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(homeViewModel.isLoading.value)  // Loading state should be true during the fetch
        assertTrue(homeViewModel.employees.value is NetworkResult.Success)
        assertEquals(mockUserList, (homeViewModel.employees.value as NetworkResult.Success).data)
    }

    @Test
    fun `fetchUsers should handle error and update state with error message`() = runTest {
        // Given
        val errorMessage = "Network error"

        // Mock repository to throw an exception
        Mockito.`when`(employeeRepository.getUsers(2)).thenThrow(Exception(errorMessage))

        // When
        homeViewModel.fetchUsers(2)

        // Advance the dispatcher to ensure the coroutines are executed
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(homeViewModel.isLoading.value)  // Loading state should be true during the fetch
        assertNull(homeViewModel.employees.value)  // Employees list should be empty
        assertEquals(errorMessage, homeViewModel.error.value)  // Error message should be set
    }

    @Test
    fun `fetchUsers should update employees with error result when no data is returned`() =
        runTest {
            // Given
            val emptyUserList = emptyList<User>()
            val employeesResponse = EmployeesResponse(emptyUserList)

            // Mock the repository to return empty data
            coEvery { employeeRepository.getUsers(2) } returns employeesResponse

            // When
            homeViewModel.fetchUsers(2)

            // Advance the dispatcher to ensure the coroutines are executed
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertTrue(homeViewModel.isLoading.value)  // Loading state should be true during the fetch
            assertTrue(homeViewModel.employees.value is NetworkResult.Error)  // Should be in error state due to empty data
        }

    @Test
    fun `fetchUsers should set loading to false after request finishes`() = runTest {
        // Given

        val employeesResponse = EmployeesResponse(UserUtils.createSampleUserList())

        // Mock repository response
        coEvery { employeeRepository.getUsers(2) } returns employeesResponse

        // When
        homeViewModel.fetchUsers(2)

        // Advance the dispatcher to ensure the coroutines are executed
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(homeViewModel.isLoading.value)  // Loading should be false after the request completes
    }

    @After
    fun tearDown() {
        // Reset the dispatcher
        Dispatchers.resetMain()
    }
}
