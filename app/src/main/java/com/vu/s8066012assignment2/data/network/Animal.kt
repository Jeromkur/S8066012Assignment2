package com.vu.s8066012assignment2.data.network

// Represents one animal returned by the API.
data class Animal(
    val species: String,
    val scientificName: String,
    val habitat: String,
    val diet: String,
    val conservationStatus: String,
    val averageLifespan: Int,
    val description: String
)