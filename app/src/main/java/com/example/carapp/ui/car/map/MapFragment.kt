package com.example.carapp.ui.car.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.carapp.ui.base.ScopedFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MapFragment : ScopedFragment(), KodeinAware, OnMapReadyCallback {
    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        googleMap.isMyLocationEnabled = true
        val kaunas = LatLng(54.8985, 23.9036)
//        //googleMap.addMarker(MarkerOptions().position(kaunas))
        val cameraPosition = CameraPosition.Builder().target(kaunas).zoom(12f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        initUI()
    }

    override val kodein by closestKodein()
    private lateinit var googleMap: GoogleMap

    lateinit var mapVieww: MapView
    private lateinit var viewModel: MapViewModel
    private val viewModelFactory: MapViewModelFactory by instance()


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            com.example.carapp.R.layout.map_fragment, container,
            false
        )
        mapVieww = v.findViewById(com.example.carapp.R.id.mapView) as MapView
        mapVieww.onCreate(savedInstanceState)

        mapVieww.onResume()// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapVieww.getMapAsync(this)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
    }

    private fun initUI() {

        val filteredCars = viewModel.filteredCars
        filteredCars.observe(this@MapFragment, Observer { carsList ->
            if (carsList == null) return@Observer
            for (car in carsList) {
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            car.location.latitude,
                            car.location.longitude
                        )
                    ).title(car.batteryPercentage.toString())
                )
            }
        })
    }


    override fun onResume() {
        super.onResume()
        mapVieww.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapVieww.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapVieww.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapVieww.onLowMemory()
    }


}
