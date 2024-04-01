package com.example.weather_app_belyakov.network

import com.example.weather_app_belyakov.model.CityResponseApi
import com.example.weather_app_belyakov.model.CurrentResponseApi
import com.example.weather_app_belyakov.model.ForecastResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("geo/1.0/direct")
    fun getCitiesList(
        @Query("q") q: String,
        @Query("limit") limit:Int,
        @Query("appid") apikey: String
    ): Call<CityResponseApi>

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apikey: String
    ): Call<CurrentResponseApi>

    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apikey: String
    ): Call<ForecastResponseApi>
}