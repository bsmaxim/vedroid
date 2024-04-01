package com.example.weather_app_belyakov.view_model

import androidx.lifecycle.ViewModel
import com.example.weather_app_belyakov.network.ApiClient
import com.example.weather_app_belyakov.network.ApiServices
import com.example.weather_app_belyakov.repository.WeatherRepository

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {
    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadGeoInfo(cityName: String) =
        repository.getGeoDecode(cityName)

    fun loadCurrentWeatherInfo(lat: Double, lon: Double, units: String) =
        repository.getCurrentWeather(lat, lon, units)
}