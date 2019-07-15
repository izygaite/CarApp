package com.example.carapp.ui.car.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.carapp.R
import com.example.carapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.car_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
//        val api = CarApi(ConnectivityInterceptorImpl(this.context!!))
//        val carNetworkDataSource = CarNetworkDataSourceImpl(api)
//        carNetworkDataSource.downloadedAvailableCars.observe(this, Observer {
//            textView.text = it.toString()
//        })
//        GlobalScope.launch(Dispatchers.Main) {
//            carNetworkDataSource.fetchAvailableCars()
//        }
    }

    private fun bindUI() = GlobalScope.launch(Dispatchers.Main) {
        val cars = viewModel.cars.await()
        cars.observe(this@CarListFragment, Observer { carsList ->
            if (carsList == null) return@Observer
            textView.text = carsList.toString()
            group_loading.visibility = View.GONE
        })
    }


}
