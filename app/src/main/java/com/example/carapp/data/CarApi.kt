package com.example.carapp.data

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
     fun getAvailableCars(): Call <List<Car>>

    companion object{
        operator fun invoke():CarApi{
            return Retrofit.Builder()
                .baseUrl("https://development.espark.lt/api/mobile/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CarApi::class.java)

        }
    }

}
