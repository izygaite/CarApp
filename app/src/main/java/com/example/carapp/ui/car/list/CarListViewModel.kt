package com.example.carapp.ui.car.list

import androidx.lifecycle.ViewModel
import com.example.carapp.data.network.CarRepository

class CarListViewModel(private val carRepository: CarRepository) : ViewModel() {
    val cars by lazy {
        carRepository.getAvailableCars()
    }
    val location by lazy {
        carRepository.getLocation().value!!
    }
}
