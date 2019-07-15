package com.example.carapp.ui.car.list

import androidx.lifecycle.ViewModel
import com.example.carapp.data.network.CarRepository
import com.example.carapp.internal.lazyDeferred

class CarListViewModel(private val carRepository:CarRepository) : ViewModel() {
    val cars by lazyDeferred {
        carRepository.getAvailableCars()
    }

}
