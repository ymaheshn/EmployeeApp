import com.example.employeeapp.api.NetworkResult
import com.example.employeeapp.model.Dob
import com.example.employeeapp.model.Location
import com.example.employeeapp.model.Name
import com.example.employeeapp.model.Picture
import com.example.employeeapp.model.Street
import com.example.employeeapp.model.User
import com.example.employeeapp.repository.EmployeeRepository
import com.example.employeeapp.viewmodels.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private var homeViewModel: HomeViewModel = mockk()
    private lateinit var employeeRepository: EmployeeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        employeeRepository = mockk()
        homeViewModel = HomeViewModel(employeeRepository)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the original dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUsers should update employees with success result`() = runTest {
        // Given
        val numberOfEmployees = 2
        val mockUserList = listOf(
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
        )

        // Mock the repository call to return a success
        coEvery { employeeRepository.getUsers(numberOfEmployees) } returns mockk {
            every { results } returns mockUserList
        }

        // When
        homeViewModel.fetchUsers(numberOfEmployees)

        // Advance the dispatcher to ensure all coroutines are executed
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        // Verify that the state is updated with the correct values
        assertTrue(homeViewModel.isLoading.value)  // Loading state should be true during the fetch
        assertTrue(homeViewModel.employees.value is NetworkResult.Success)
        assertEquals(mockUserList, (homeViewModel.employees.value as NetworkResult.Success).data)
    }

    @Test
    fun `fetchUsers should handle error and update state with error message`() = runTest {
        // Given
        val numberOfEmployees = 2
        val errorMessage = "Network error"

        // Mock the repository to throw an exception
        coEvery { employeeRepository.getUsers(numberOfEmployees) } throws Exception(errorMessage)

        // When
        homeViewModel.fetchUsers(numberOfEmployees)

        // Advance the dispatcher to ensure all coroutines are executed
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        // Verify that error state is populated
        assertTrue(homeViewModel.isLoading.value)  // Loading state should be true during the fetch
        assertNull(homeViewModel.employees.value)  // Employees list should be empty
        assertEquals(errorMessage, homeViewModel.error.value)  // Error message should be set
    }

    @Test
    fun `fetchUsers should update employees with error result when no data is returned`() =
        runTest {
            // Given
            val numberOfEmployees = 2
            val emptyUserList = emptyList<User>()

            // Mock the repository to return an empty result
            coEvery { employeeRepository.getUsers(numberOfEmployees) } returns mockk {
                every { results } returns emptyUserList
            }

            // When
            homeViewModel.fetchUsers(numberOfEmployees)

            // Advance the dispatcher to ensure all coroutines are executed
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            // Verify that the state is updated with an error due to empty data
            assertTrue(homeViewModel.isLoading.value)  // Loading state should be true during the fetch
            assertTrue(homeViewModel.employees.value is NetworkResult.Error)
        }
}
