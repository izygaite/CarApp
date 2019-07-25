package com.example.carapp.data.network

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.carapp.data.Car
import com.example.carapp.internal.FilterState

interface CarRepository {
    fun getAvailableCars(): LiveData<List<Car>>
    fun getFilteredCars(filterState: FilterState): LiveData<List<Car>>
    fun getLocation(): MutableLiveData<Location>
}