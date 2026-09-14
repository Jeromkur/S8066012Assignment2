package com.vu.s8066012assignment2

import com.vu.s8066012assignment2.data.network.ApiService
import com.vu.s8066012assignment2.data.network.DashboardResponse
import com.vu.s8066012assignment2.data.network.LoginRequest
import com.vu.s8066012assignment2.data.network.LoginResponse
import com.vu.s8066012assignment2.data.repository.AuthRepository
import com.vu.s8066012assignment2.ui.login.LoginUiState
import com.vu.s8066012assignment2.ui.login.LoginViewModel
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
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successfulLoginReturnsExpectedKeypass() = runTest {
        // Arrange: the fake API will return "animals".
        val api = FakeLoginApi()
        val viewModel = LoginViewModel(AuthRepository(api))

        // Act: submit sample credentials.
        viewModel.login("1234567", "TestPassword")

        assertEquals(LoginUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()

        // Assert: the ViewModel exposes the returned keypass.
        assertEquals(
            LoginUiState.Success("animals"),
            viewModel.uiState.value
        )
        assertEquals("1234567", api.receivedRequest?.username)
        assertEquals("TestPassword", api.receivedRequest?.password)
    }

    @Test
    fun networkFailureProducesFriendlyError() = runTest {
        // Arrange: simulate a connection failure.
        val api = FakeLoginApi(failWithNetworkError = true)
        val viewModel = LoginViewModel(AuthRepository(api))

        // Act.
        viewModel.login("1234567", "TestPassword")
        advanceUntilIdle()

        // Assert: the failure becomes a helpful error state.
        assertEquals(
            LoginUiState.Error(
                "Connection failed. Check your internet and try again."
            ),
            viewModel.uiState.value
        )
    }

    private class FakeLoginApi(
        private val failWithNetworkError: Boolean = false
    ) : ApiService {

        var receivedRequest: LoginRequest? = null

        override suspend fun login(
            request: LoginRequest
        ): LoginResponse {
            receivedRequest = request

            if (failWithNetworkError) {
                throw IOException("Simulated connection failure")
            }

            return LoginResponse(keypass = "animals")
        }

        override suspend fun getDashboard(
            keypass: String
        ): DashboardResponse {
            error("Dashboard is not used by these login tests.")
        }
    }
}