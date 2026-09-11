package com.vu.s8066012assignment2.data.network

// Data sent to the API when logging in.
data class LoginRequest(
    val username: String,
    val password: String
)