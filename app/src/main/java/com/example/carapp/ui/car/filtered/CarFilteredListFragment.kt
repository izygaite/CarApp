package com.example.carapp.ui.car.filtered

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.carapp.R

class CarFilteredListFragment : Fragment() {

    companion object {
        fun newInstance() = CarFilteredListFragment()
    }

    private lateinit var viewModel: CarFilteredListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_filtered_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CarFilteredListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
