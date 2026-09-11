package com.vu.s8066012assignment2.data.repository

import com.vu.s8066012assignment2.data.network.ApiService
import com.vu.s8066012assignment2.data.network.LoginRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    // Sends the credentials and returns the assigned keypass.
    suspend fun login(username: String, password: String): String {
        val request = LoginRequest(
            username = username,
            password = password
        )

        val response = apiService.login(request)
        return response.keypass
    }
}