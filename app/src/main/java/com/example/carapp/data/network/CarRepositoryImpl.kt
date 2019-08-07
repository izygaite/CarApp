package com.example.carapp.data.network

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.carapp.data.Car
import com.example.carapp.data.provider.LocationProvider
import com.example.carapp.internal.FilterState
import com.example.carapp.internal.SortType

class CarRepositoryImpl(
    private val carNetworkDataSource: CarNetworkDataSource,
    private val locationProvider: LocationProvider
) : CarRepository {
    override fun getLocation(): MutableLiveData<android.location.Location> {
        return locationProvider.fetchLocation()
    }

    override fun getFilteredCars(filterState: FilterState): LiveData<List<Car>> {
        val temp = carNetworkDataSource.downloadedAvailableCars
        val location: MutableLiveData<Location>? = locationProvider.fetchLocation()
        var filtered = temp
        if (!filterState.plateNumberQuery.isNullOrEmpty()) {
            filtered = getCarsFilteredByPlateNumber(filterState.plateNumberQuery!!, filtered)
        }
        if (filterState.batteryLevelQuery != null && filterState.batteryLevelQuery!! > 0) {
            filtered = getCarsFilteredByBatteryLevel(filterState.batteryLevelQuery!!, filtered)
        }
        if (filterState.sortQuery != null && location != null && location.value != null) {
            filtered = sortCars(filterState.sortQuery!!, filtered, location.value!!)
        }
        return filtered
    }

    override fun getAvailableCars(): LiveData<List<Car>> {
        carNetworkDataSource.fetchAvailableCars()
        return carNetworkDataSource.downloadedAvailableCars
    }

    private fun getCarsFilteredByPlateNumber(plateNumber: String, temp: LiveData<List<Car>>): LiveData<List<Car>> {
        val filteredCars = MutableLiveData<List<Car>>()
        return Transformations.switchMap(temp) { carList ->
            val filteredList =
                carList.filter { car ->
                    car.plateNumber.toLowerCase().contains(plateNumber)
                }
            filteredCars.value = filteredList
            filteredCars
        }
    }

    private fun getCarsFilteredByBatteryLevel(batteryLevel: Int, temp: LiveData<List<Car>>): LiveData<List<Car>> {
        val filteredCars = MutableLiveData<List<Car>>()
        return Transformations.switchMap(temp) { carList ->
            val filteredList =
                carList.filter { car ->
                    car.batteryPercentage > batteryLevel
                }
            filteredCars.value = filteredList
            filteredCars
        }

    }

    private fun sortCars(
        sort: SortType,
        temp: LiveData<List<Car>>,
        location: android.location.Location
    ): LiveData<List<Car>> {
        val sortedCars = MutableLiveData<List<Car>>()
        return Transformations.switchMap(temp) { carList ->
            val sortedList =
                if (sort == SortType.ASC)
                    carList.sortedBy { it.distance(location) }
                else carList.sortedByDescending { it.distance(location) }
            sortedCars.value = sortedList
            sortedCars
        }
    }
}