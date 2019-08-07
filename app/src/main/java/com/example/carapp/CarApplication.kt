package com.example.carapp

import android.app.Application
import android.content.Context
import com.example.carapp.data.network.*
import com.example.carapp.data.provider.LocationProvider
import com.example.carapp.data.provider.LocationProviderImpl
import com.example.carapp.ui.car.list.CarListViewModelFactory
import com.example.carapp.ui.car.map.MapViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class CarApplication : Application(), KodeinAware {
    override val kodein: Kodein = lazy {
        import(androidXModule(this@CarApplication))
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { CarApi(instance()) }
        bind<CarNetworkDataSource>() with singleton { CarNetworkDataSourceImpl(instance()) }
        bind<CarRepository>() with singleton { CarRepositoryImpl(instance(), instance()) }
        bind() from provider { CarListViewModelFactory(instance()) }
        bind() from provider { MapViewModelFactory(instance()) }
        bind() from provider { FusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
    }
}