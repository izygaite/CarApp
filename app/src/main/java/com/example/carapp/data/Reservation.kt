package com.example.carapp.data

data class Reservation(
    val extensionMinutes: Int,
    val extensionPrice: Int,
    val initialMinutes: Int,
    val initialPrice: Int,
    val longerExtensionMinutes: Int,
    val longerExtensionPrice: Int
)