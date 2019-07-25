package com.example.carapp.ui.car.filtered

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carapp.data.network.CarRepository

class CarFilteredListViewModelFactory(private val carRepository: CarRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CarFilteredListViewModel(carRepository) as T
    }
}