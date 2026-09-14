package com.vu.s80666012assignment2

import com.vu.s8066012assignment2.data.network.Animal
import com.vu.s8066012assignment2.data.network.ApiService
import com.vu.s8066012assignment2.data.network.DashboardResponse
import com.vu.s8066012assignment2.data.network.LoginRequest
import com.vu.s8066012assignment2.data.network.LoginResponse
import com.vu.s8066012assignment2.data.repository.DashboardRepository
import com.vu.s8066012assignment2.ui.dashboard.DashboardUiState
import com.vu.s8066012assignment2.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successfulLoadingReturnsAnimalsAndTotal() = runTest {
        // Arrange: prepare sample data returned by the fake API.
        val expectedAnimals = listOf(
            Animal(
                species = "African Elephant",
                scientificName = "Loxodonta africana",
                habitat = "Savanna",
                diet = "Herbivore",
                conservationStatus = "Vulnerable",
                averageLifespan = 60,
                description = "The largest land animal."
            ),
            Animal(
                species = "Giant Panda",
                scientificName = "Ailuropoda melanoleuca",
                habitat = "Temperate forests",
                diet = "Herbivore",
                conservationStatus = "Vulnerable",
                averageLifespan = 20,
                description = "A bear native to China."
            )
        )

        val api = FakeDashboardApi(
            DashboardResponse(
                entities = expectedAnimals,
                entityTotal = 2
            )
        )
        val viewModel = DashboardViewModel(DashboardRepository(api))

        // Act: request the dashboard using a keypass.
        viewModel.loadAnimals("animals")

        assertEquals(
            DashboardUiState.Loading,
            viewModel.uiState.value
        )
        advanceUntilIdle()

        // Assert: the list and total match the API response.
        assertEquals(
            DashboardUiState.Success(
                animals = expectedAnimals,
                total = 2
            ),
            viewModel.uiState.value
        )
        assertEquals("animals", api.receivedKeypass)
    }

    private class FakeDashboardApi(
        private val response: DashboardResponse
    ) : ApiService {

        var receivedKeypass: String? = null

        override suspend fun getDashboard(
            keypass: String
        ): DashboardResponse {
            receivedKeypass = keypass
            return response
        }

        override suspend fun login(
            request: LoginRequest
        ): LoginResponse {
            error("Login is not used by this dashboard test.")
        }
    }
}
