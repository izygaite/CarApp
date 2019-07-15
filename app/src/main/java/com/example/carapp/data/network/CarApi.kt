package com.example.carapp.data.network

import com.example.carapp.data.Car
import com.example.carapp.internal.BASE_URI
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 */
interface CarApi {
    /**
     * Get the list of the available cars from the API
     */
    @GET("availablecars")
    fun getAvailableCars(): Call<List<Car>>

    companion object {

        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): CarApi {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CarApi::class.java)

        }
    }
}

