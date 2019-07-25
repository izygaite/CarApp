package com.example.carapp.ui.car.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carapp.R
import com.example.carapp.data.Car
import com.example.carapp.ui.base.ScopedFragment
import com.example.carapp.ui.car.CarItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.car_list_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CarListFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CarListViewModelFactory by instance()

    private lateinit var viewModel: CarListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CarListViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {
        val cars = viewModel.cars
        cars.observe(this@CarListFragment, Observer { carsList ->
            if (carsList == null) return@Observer
            group_loading.visibility = View.GONE
            initRecyclerView(carsList.toCarItems())
        })
    }

    private fun initRecyclerView(items: List<CarItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply { addAll(items) }
        listRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CarListFragment.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, view ->
            Toast.makeText(this@CarListFragment.context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun List<Car>.toCarItems(): List<CarItem> {
        return this.map { CarItem(it, viewModel.location) }
    }


}
