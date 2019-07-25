package com.example.carapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.carapp.data.Car
import com.example.carapp.internal.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarNetworkDataSourceImpl(private val carApi: CarApi) : CarNetworkDataSource {
    private val _downloadedAvailableCars = MutableLiveData<List<Car>>()
    override val downloadedAvailableCars: LiveData<List<Car>> =
        Transformations.map(_downloadedAvailableCars) { car -> car }

    override fun fetchAvailableCars() {
        try {
            carApi.getAvailableCars().enqueue(object : Callback<List<Car>> {
                override fun onFailure(call: Call<List<Car>>?, t: Throwable?) {
                    Log.v("Connectivity", "getAvailableCars() call failed")
                }

                override fun onResponse(call: Call<List<Car>>?, response: Response<List<Car>>?) {
                    _downloadedAvailableCars.postValue(response!!.body()!!)
                }
            })

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}