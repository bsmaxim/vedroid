package com.example.weather_app_belyakov.network

import com.example.weather_app_belyakov.model.OpenWeatherCurrentResponse
import com.example.weather_app_belyakov.model.OpenWeatherGeoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherServices {
    @GET("geo/1.0/direct")
    fun getGeoLocation(
        @Query("city") city: String,
        @Query("appid") apikey: String
    ): Call<OpenWeatherGeoResponse>

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apikey: String
    ): Call<OpenWeatherCurrentResponse>
}