package com.vu.s8066012assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vu.s8066012assignment2.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Represents what the Login screen should display.
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val keypass: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        // Prevents duplicate requests while login is running.
        if (_uiState.value is LoginUiState.Loading) return

        if (username.isBlank() || password.isBlank()) {
            _uiState.value =
                LoginUiState.Error("Please enter your student ID and password.")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val keypass = repository.login(username.trim(), password)

                _uiState.value = if (keypass.isBlank()) {
                    LoginUiState.Error("The server returned an empty keypass.")
                } else {
                    LoginUiState.Success(keypass)
                }
            } catch (e: CancellationException) {
                // Allows cancelled coroutines to stop normally.
                throw e
            } catch (e: HttpException) {
                val message = when (e.code()) {
                    401, 403 -> "Incorrect student ID or password."
                    else -> "Login failed. Please try again."
                }
                _uiState.value = LoginUiState.Error(message)
            } catch (e: IOException) {
                _uiState.value =
                    LoginUiState.Error("Connection failed. Check your internet and try again.")
            } catch (e: Exception) {
                _uiState.value =
                    LoginUiState.Error("Something went wrong. Please try again.")
            }
        }
    }
}