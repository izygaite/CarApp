package com.example.carapp.data.network

import androidx.lifecycle.LiveData
import com.example.carapp.data.Car

class CarRepositoryImpl(private val carNetworkDataSource: CarNetworkDataSource) : CarRepository {

    override suspend fun getAvailableCars(): LiveData<List<Car>> {
        carNetworkDataSource.fetchAvailableCars()
        return carNetworkDataSource.downloadedAvailableCars
    }
}