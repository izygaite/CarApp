package com.example.carapp.data

data class Car(
    val batteryEstimatedDistance: Int,
    val batteryPercentage: Int,
    val id: Int,
    val isCharging: Boolean,
    val location: Location,
    val model: Model,
    val plateNumber: String
) {
    fun distance(deviceLocation: android.location.Location): Float {
        val currentLocation = android.location.Location("")
        currentLocation.latitude = location.latitude
        currentLocation.longitude = location.longitude
        return currentLocation.distanceTo(deviceLocation) / 1000
    }
}