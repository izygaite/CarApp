package com.example.carapp.data

data class Rate(
    val currency: String,
    val currencySymbol: String,
    val isWeekend: Boolean,
    val lease: Lease,
    val reservation: Reservation
)