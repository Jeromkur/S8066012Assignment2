package com.vu.s8066012assignment2.data.network

// Contains the animal list and its total count.
data class DashboardResponse(
    val entities: List<Animal>,
    val entityTotal: Int
)