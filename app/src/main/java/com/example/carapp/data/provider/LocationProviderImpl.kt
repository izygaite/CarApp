package com.example.carapp.data.provider

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

class LocationProviderImpl(
    private var fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : LocationProvider {
    override fun fetchLocation(): MutableLiveData<Location> {
        return locationData!!
    }

    private var mLocationRequest: LocationRequest? = null
    private val appContext = context.applicationContext
    private var locationData: MutableLiveData<Location>? = MutableLiveData()
    private var mLocationCallback: LocationCallback? = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null)
                    locationData!!.value = location
            }
        }
    }

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
        createLocationRequest()
        if (ContextCompat.checkSelfPermission(
                appContext,
                permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    locationData!!.value = task.result
                } else {
                    fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback!!, null)
                }
            }
        }

    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}