package com.example.weather_app_belyakov.repository

import com.example.weather_app_belyakov.network.OpenWeatherServices

class OpenWeatherRepository(val api: OpenWeatherServices) {
    private val ApiKey = "531a96ca0c18a0627ebab2a031ef5331"

    fun getGeoDecode(cityName: String)=
        api.getGeoLocation(cityName, ApiKey)

    fun getCurrentWeather(lat: Double, lon: Double, units: String)=
        api.getCurrentWeather(lat, lon, units, ApiKey)
}