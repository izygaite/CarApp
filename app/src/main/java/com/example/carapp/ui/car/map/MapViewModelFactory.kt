package com.example.carapp.ui.car.map


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carapp.data.network.CarRepository

class MapViewModelFactory(private val carRepository: CarRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(carRepository) as T
    }
}