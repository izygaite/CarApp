package com.example.carapp.ui.car.filtered

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carapp.R
import com.example.carapp.data.Car
import com.example.carapp.internal.SortType
import com.example.carapp.ui.base.ScopedFragment
import com.example.carapp.ui.car.CarItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.car_filtered_list_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CarFilteredListFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CarFilteredListViewModelFactory by instance()

    private lateinit var viewModel: CarFilteredListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_filtered_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CarFilteredListViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {

        val filteredCars = viewModel.filteredCars
        filteredCars.observe(this@CarFilteredListFragment, Observer { carsList ->
            if (carsList == null) return@Observer
            filtered_group_loading.visibility = View.GONE
            initRecyclerView(carsList.toCarItems())
        })

        plateNumberInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setPlateNumberQuery(p0.toString())

            }
        })
        var sort = true
        sortButton.setOnClickListener {
            if (sort)
                viewModel.setSort(SortType.ASC)
            else
                viewModel.setSort(SortType.DESC)
            sort = !sort

        }
        batteryLevelInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //  viewModel.setFilter(p0.toString())
                viewModel.setBatteryLevelQuery(p0.toString().toInt())

            }
        })


    }

    private fun initRecyclerView(items: List<CarItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply { addAll(items) }
        filteredListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CarFilteredListFragment.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { _, _ ->
            Toast.makeText(this@CarFilteredListFragment.context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun List<Car>.toCarItems(): List<CarItem> {
        return this.map { CarItem(it, viewModel.location) }
    }

}
