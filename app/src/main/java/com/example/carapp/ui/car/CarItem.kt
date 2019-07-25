package com.example.carapp.ui.car

import android.location.Location
import com.example.carapp.R
import com.example.carapp.data.Car
import com.example.carapp.internal.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_car.*

class CarItem(val car: Car, val location: Location) : Item() {
    override fun getLayout(): Int = R.layout.item_car
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_distance.text = car.distance(location).toString()
            textView_id.text = car.id.toString()
            textView_plateNumber.text = car.plateNumber
            textView_batteryLevel.text = car.batteryPercentage.toString()
            updatePhoto()
        }
    }

    private fun ViewHolder.updatePhoto() {
        GlideApp.with(this.containerView)
            .load(car.model.photoUrl)
            .into(imageView_photoUrl)
    }

}