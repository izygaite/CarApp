package com.example.carapp.ui.car.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carapp.data.network.CarRepository

class CarListViewModelFactory(private val carRepository: CarRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CarListViewModel(carRepository) as T
    }
}