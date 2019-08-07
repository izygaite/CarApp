package com.example.carapp.ui.car

import android.annotation.SuppressLint
import android.location.Location
import com.example.carapp.R
import com.example.carapp.data.Car
import com.example.carapp.internal.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_car.*

class CarItem(val car: Car, val location: Location?) : Item() {
    override fun getLayout(): Int = R.layout.item_car
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            if (location != null) {
                textView_distance.text = "${String.format("%2.1f", car.distance(location))} km"
            } else {
                textView_distance.text = ""
            }
            textView_location.text = car.location.address
            textView_modelInfo.text = car.model.title
            textView_plateNumber.text = car.plateNumber
            textView_batteryLevel.text = "${car.batteryPercentage}%"
            updatePhoto()
        }
    }

    private fun ViewHolder.updatePhoto() {
        GlideApp.with(this.containerView)
            .load(car.model.photoUrl)
            .centerInside()
            .into(imageView_photoUrl)
    }
}