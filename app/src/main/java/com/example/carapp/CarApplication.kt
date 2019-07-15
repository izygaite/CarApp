package com.example.carapp

import android.app.Application
import com.example.carapp.data.network.*
import com.example.carapp.ui.car.list.CarListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.Provider
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CarApplication:Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidModule(this@CarApplication))
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { CarApi(instance()) }
        bind<CarNetworkDataSource>() with singleton { CarNetworkDataSourceImpl(instance()) }
        bind<CarRepository>() with singleton { CarRepositoryImpl(instance()) }
        bind() from provider { CarListViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
    }
}