package com.example.carapp.data.network

import androidx.lifecycle.LiveData
import com.example.carapp.data.Car

interface CarRepository{
    suspend fun getAvailableCars():LiveData<List<Car>>
}