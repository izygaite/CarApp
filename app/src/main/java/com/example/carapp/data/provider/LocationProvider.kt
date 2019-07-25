package com.example.carapp.data.provider

import android.location.Location
import androidx.lifecycle.MutableLiveData

interface LocationProvider {
    fun fetchLocation(): MutableLiveData<Location>
}