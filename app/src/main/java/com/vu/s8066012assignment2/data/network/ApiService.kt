package com.vu.s8066012assignment2.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Sends credentials and receives the keypass.
    @POST("footscray/auth")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // Uses the returned keypass to fetch the animal dataset.
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): DashboardResponse
}