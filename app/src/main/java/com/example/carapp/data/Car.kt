package com.example.carapp.data

data class Car(
    val batteryEstimatedDistance: Int,
    val batteryPercentage: Int,
    val id: Int,
    val isCharging: Boolean,
    val location: Location,
    val model: Model,
    val plateNumber: String,
    val rate: Rate
)