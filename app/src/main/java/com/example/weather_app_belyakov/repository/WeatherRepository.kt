package com.example.weather_app_belyakov.repository

import com.example.weather_app_belyakov.network.ApiServices

class WeatherRepository(val api: ApiServices) {
    fun getCurrentWeather(lat: Double, lon: Double, units: String)=
        api.getCurrentWeather(lat, lon, units, "531a96ca0c18a0627ebab2a031ef5331")

    fun getForecastWeather(lat: Double, lon: Double, units: String)=
        api.getForecastWeather(lat, lon, units, "531a96ca0c18a0627ebab2a031ef5331")
}