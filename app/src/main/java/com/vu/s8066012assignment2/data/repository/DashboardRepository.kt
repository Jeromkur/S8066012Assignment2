package com.vu.s8066012assignment2.data.repository

import com.vu.s8066012assignment2.data.network.ApiService
import com.vu.s8066012assignment2.data.network.DashboardResponse
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val apiService: ApiService
) {

    // Fetches dashboard data using the keypass received at login.
    suspend fun getDashboard(keypass: String): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}