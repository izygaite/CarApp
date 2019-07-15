package com.example.carapp.data.network

import androidx.lifecycle.LiveData
import com.example.carapp.data.Car

interface CarNetworkDataSource {
    val downloadedAvailableCars: LiveData<List<Car>>
    suspend fun fetchAvailableCars()
}