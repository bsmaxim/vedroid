package com.example.weather_app_belyakov.repository

import com.example.weather_app_belyakov.network.ApiServices

class WeatherRepository(val api: ApiServices) {
    fun getGeoDecode(cityName: String)=
        api.getGeoLocation(cityName, "531a96ca0c18a0627ebab2a031ef5331")

    fun getCurrentWeather(lat: Double, lon: Double, units: String)=
        api.getCurrentWeather(lat, lon, units, "531a96ca0c18a0627ebab2a031ef5331")
}