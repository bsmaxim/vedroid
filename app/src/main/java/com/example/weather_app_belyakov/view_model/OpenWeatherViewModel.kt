package com.example.weather_app_belyakov.view_model

import androidx.lifecycle.ViewModel
import com.example.weather_app_belyakov.network.OpenWeatherApi
import com.example.weather_app_belyakov.network.OpenWeatherServices
import com.example.weather_app_belyakov.repository.OpenWeatherRepository

class OpenWeatherViewModel(val repository: OpenWeatherRepository) : ViewModel() {
    constructor() : this(OpenWeatherRepository(OpenWeatherApi().getClient().create(OpenWeatherServices::class.java)))

    fun loadGeoInfo(cityName: String) =
        repository.getGeoDecode(cityName)

    fun loadCurrentWeatherInfo(lat: Double, lon: Double, units: String) =
        repository.getCurrentWeather(lat, lon, units)
}