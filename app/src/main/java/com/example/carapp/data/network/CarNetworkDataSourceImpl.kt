package com.example.carapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.carapp.data.Car
import com.example.carapp.internal.NoConnectivityException
import retrofit2.await

class CarNetworkDataSourceImpl(private val carApi: CarApi) : CarNetworkDataSource {
    private val _downloadedAvailableCars = MutableLiveData<List<Car>>()
    override val downloadedAvailableCars: LiveData<List<Car>> =
        Transformations.map(_downloadedAvailableCars) { car -> car }

    override suspend fun fetchAvailableCars() {
        try {
            val cars = carApi.getAvailableCars().await()
            _downloadedAvailableCars.postValue(cars)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)

        }
    }
}