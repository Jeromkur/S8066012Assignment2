package com.vu.s8066012assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vu.s8066012assignment2.data.network.Animal
import com.vu.s8066012assignment2.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Represents the different states of the Dashboard screen.
sealed interface DashboardUiState {
    data object Idle : DashboardUiState
    data object Loading : DashboardUiState

    data class Success(
        val animals: List<Animal>,
        val total: Int
    ) : DashboardUiState

    data class Error(val message: String) : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)

    val uiState = _uiState.asStateFlow()

    fun loadAnimals(keypass: String, forceRefresh: Boolean = false) {
        // Avoids duplicate requests and reloading after screen rotation.
        if (_uiState.value is DashboardUiState.Loading) return
        if (!forceRefresh && _uiState.value is DashboardUiState.Success) return

        if (keypass.isBlank()) {
            _uiState.value =
                DashboardUiState.Error("Missing keypass. Please log in again.")
            return
        }

        _uiState.value = DashboardUiState.Loading

        viewModelScope.launch {
            try {
                val response = repository.getDashboard(keypass)

                _uiState.value = DashboardUiState.Success(
                    animals = response.entities,
                    total = response.entityTotal
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                _uiState.value =
                    DashboardUiState.Error("Unable to load animals. Please try again.")
            } catch (e: IOException) {
                _uiState.value =
                    DashboardUiState.Error("Connection failed. Check your internet.")
            } catch (e: Exception) {
                _uiState.value =
                    DashboardUiState.Error("Something went wrong. Please try again.")
            }
        }
    }
}