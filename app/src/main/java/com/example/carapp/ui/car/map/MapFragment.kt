package com.example.carapp.ui.car.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.carapp.R
import com.example.carapp.data.Car
import com.example.carapp.ui.base.ScopedFragment
import com.example.carapp.ui.car.CarItem
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.map_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MapFragment : ScopedFragment(), KodeinAware, OnMapReadyCallback {
    private var carMarkersList = mutableListOf<Marker>()
    override val kodein by closestKodein()
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var viewModel: MapViewModel
    private val viewModelFactory: MapViewModelFactory by instance()

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        googleMap.isMyLocationEnabled = true
        val startingLocation = LatLng(54.6872, 25.2797)
        val cameraPosition = CameraPosition.Builder().target(startingLocation).zoom(12f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        initUI()
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.map_fragment, container,
            false
        )
        mapView = v.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView.getMapAsync(this)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.title_map)
    }

    private fun initUI() {
        val filteredCars = viewModel.filteredCars
        filteredCars.observe(this@MapFragment, Observer { carsList ->
            if (carsList == null) return@Observer
            initRecyclerView(carsList.toCarItems())
            group_listLoading.visibility = View.GONE
            googleMap.clear()
            carMarkersList.clear()
            for (car in carsList) {
                val gson = Gson()
                val carString = gson.toJson(car)
                val marker = googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            car.location.latitude,
                            car.location.longitude
                        )
                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)).snippet(carString)
                )
                marker.tag = carMarkersList.size
                carMarkersList.add(marker)
            }
            if (carsList.isEmpty()) {
                Toast.makeText(
                    this@MapFragment.context,
                    resources.getString(R.string.cannot_find_cars_for_this_filter_try_changing_the_filter),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        googleMap.setOnMarkerClickListener {
            val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = it.tag as Int
            recyclerView_carListMap.layoutManager!!.startSmoothScroll(smoothScroller)
            carMarkersList[it.tag as Int].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_current))
            for (i in 0 until carMarkersList.size) {
                if (i != it.tag as Int) {
                    carMarkersList[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
                }
            }

            true
        }
        seekBar_batteryLevel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val value = progress * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max
                textView_batteryLevelProgress.text = progress.toString()
                textView_batteryLevelProgress.x = seekBar.x + value + seekBar.thumbOffset / 2
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setBatteryLevelQuery(seekBar.progress)
            }
        })
    }

    private fun initRecyclerView(items: List<CarItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply { addAll(items) }
        recyclerView_carListMap.apply {
            layoutManager = LinearLayoutManager(this@MapFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, _ ->
            val itemPosition = groupAdapter.getAdapterPosition(item)
            val markerPosition = carMarkersList[itemPosition].position
            carMarkersList[itemPosition].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_current))
            val cameraPosition = CameraPosition.Builder().target(markerPosition).zoom(12f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            for (i in 0 until carMarkersList.size) {
                if (i != itemPosition) {
                    carMarkersList[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
                }
            }
        }
    }

    private fun List<Car>.toCarItems(): List<CarItem> {
        return this.map { CarItem(it, viewModel.location) }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}