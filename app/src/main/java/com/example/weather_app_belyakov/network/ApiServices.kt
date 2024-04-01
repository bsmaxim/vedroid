package com.example.weather_app_belyakov.network

import com.example.weather_app_belyakov.model.CurrentResponseApi
import com.example.weather_app_belyakov.model.GeoResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("geo/1.0/direct")
    fun getGeoLocation(
        @Query("city") city: String,
        @Query("appid") apikey: String
    ): Call<GeoResponseApi>

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apikey: String
    ): Call<CurrentResponseApi>
}