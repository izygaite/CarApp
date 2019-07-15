package com.example.carapp.data

data class Lease(
    val freeKilometersPerDay: Int,
    val kilometerPrice: Double,
    val servicePlusBatteryMaxKm: Int,
    val servicePlusBatteryMinKm: Int,
    val servicePlusEGoPoints: Int,
    val weekends: Weekends,
    val workdays: Workdays
)