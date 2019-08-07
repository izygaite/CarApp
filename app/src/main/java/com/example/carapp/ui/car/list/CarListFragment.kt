package com.example.carapp.ui.car.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carapp.data.Car
import com.example.carapp.internal.SortType
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
        return inflater.inflate(com.example.carapp.R.layout.car_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CarListViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            resources.getString(com.example.carapp.R.string.title_list)
        initButtons()
        initFilterLayout()

        val filteredCars = viewModel.filteredCars
        filteredCars.observe(this@CarListFragment, Observer { carsList ->
            if (carsList == null) return@Observer
            changeEmptyListLayoutVisibility(carsList.isEmpty())
            group_listLoading.visibility = View.GONE
            initRecyclerView(carsList.toCarItems())
        })
    }

    private fun initButtons() {
        button_filter.setOnClickListener { showFilterLayout() }
        button_sort.setOnClickListener { showPopup(it) }
    }

    private fun initFilterLayout() {
        button_applyFilter.setOnClickListener { applyFilter() }
        button_close.setOnClickListener { hideFilterLayout() }
        button_clearFilter.setOnClickListener { clearFilters() }

        seekBar_batteryLevel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView_batteryLevelProgress.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    private fun showFilterLayout() {
        if (group_filter.visibility == View.GONE) {
            group_filter.visibility = View.VISIBLE
            button_close.visibility = View.VISIBLE
            button_filter.visibility = View.GONE
            button_sort.visibility = View.GONE
        }
    }

    private fun hideFilterLayout() {
        if (group_filter.visibility != View.GONE) {
            group_filter.visibility = View.GONE
            button_filter.visibility = View.VISIBLE
            button_sort.visibility = View.VISIBLE
            button_close.visibility = View.GONE
            inputText_plateNumberInput.hideKeyboard()
        }
    }

    private fun initRecyclerView(items: List<CarItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply { addAll(items) }
        recyclerView_carList.apply {
            layoutManager = LinearLayoutManager(this@CarListFragment.context)
            adapter = groupAdapter
        }
    }

    private fun List<Car>.toCarItems(): List<CarItem> {
        return this.map { CarItem(it, viewModel.location!!.value) }
    }

    private fun showPopup(view: View) {
        val popup: PopupMenu?
        popup = PopupMenu(context!!, view)
        popup.inflate(com.example.carapp.R.menu.sort_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                com.example.carapp.R.id.sort_asc -> {
                    viewModel.setSort(SortType.ASC)
                }
                com.example.carapp.R.id.sort_desc -> {
                    viewModel.setSort(SortType.DESC)
                }
            }
            true
        })
        popup.show()
    }

    private fun clearFilters() {
        viewModel.clearFilter()
        inputText_plateNumberInput.text!!.clear()
        textView_batteryLevelProgress.text = resources.getString(com.example.carapp.R.string.zero)
        seekBar_batteryLevel.progress = 0
        inputText_plateNumberInput.hideKeyboard()
    }

    private fun changeEmptyListLayoutVisibility(isEmpty: Boolean) {
        if (isEmpty)
            layout_emptyList.visibility = View.VISIBLE
        else
            layout_emptyList.visibility = View.GONE
    }

    private fun applyFilter() {
        inputText_plateNumberInput.hideKeyboard()
        viewModel.setBatteryLevelQuery(seekBar_batteryLevel.progress)
        viewModel.setPlateNumberQuery(inputText_plateNumberInput.text.toString())
    }
}
