package com.example.carapp.ui.car.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.carapp.data.Car
import com.example.carapp.data.network.CarRepository
import com.example.carapp.internal.FilterState
import com.example.carapp.internal.SortType

class MapViewModel(private val carRepository: CarRepository) : ViewModel() {
    private val allCars = carRepository.getAvailableCars()

    var filter = MutableLiveData<FilterState>()

    val location by lazy {
        carRepository.getLocation().value!!
    }
    var filteredCars: LiveData<List<Car>>

    init {
        filter.postValue(FilterState(null, null, null))
        filteredCars = Transformations.switchMap(filter) { filter ->
            val cars = when {
                filter == null || (filter.plateNumberQuery.isNullOrEmpty() && filter.batteryLevelQuery == null && filter.sortQuery == null) -> allCars
                else -> {
                    carRepository.getFilteredCars(filter)
                }
            }
            cars
        }
    }
    fun setPlateNumberQuery(query: String) {
        filter.value!!.plateNumberQuery = query
        filter.postValue(filter.value)
    }

    fun setSort(query: SortType) {
        filter.value!!.sortQuery = query
        filter.postValue(filter.value)
    }

    fun setBatteryLevelQuery(query: Int) {
        filter.value!!.batteryLevelQuery = query
        filter.postValue(filter.value)
    }
}
