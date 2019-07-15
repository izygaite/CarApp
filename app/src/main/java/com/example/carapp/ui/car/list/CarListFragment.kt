package com.example.carapp.ui.car.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.carapp.R
import com.example.carapp.data.CarApi
import kotlinx.android.synthetic.main.car_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import retrofit2.awaitResponse

class CarListFragment : Fragment() {

    companion object {
        fun newInstance() = CarListFragment()
    }

    private lateinit var viewModel: CarListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CarListViewModel::class.java)
        // TODO: Use the ViewModel
        val api = CarApi()
        GlobalScope.launch(Dispatchers.Main) {
            val resp = api.getAvailableCars().await()
            textView.text=resp.toString()


        }
    }

}
