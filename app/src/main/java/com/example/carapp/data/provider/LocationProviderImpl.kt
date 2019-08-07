package com.example.carapp.data.provider

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient

class LocationProviderImpl(
    private var fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : LocationProvider {
    private var locationData: MutableLiveData<Location>? = MutableLiveData()

    @SuppressLint("MissingPermission")
    override fun fetchLocation(): MutableLiveData<Location> {
        if (checkPermissions()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                locationData!!.value = it
            }
        }
        return locationData!!
    }

    private val appContext = context.applicationContext

    init {
        if (checkPermissions()) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    locationData!!.value = task.result
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            appContext,
            permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }
}