package com.example.weather_app_belyakov.view_model

import androidx.lifecycle.ViewModel
import com.example.weather_app_belyakov.network.GeoapifyApi
import com.example.weather_app_belyakov.network.GeoapifyService
import com.example.weather_app_belyakov.network.OpenWeatherApi
import com.example.weather_app_belyakov.network.OpenWeatherServices
import com.example.weather_app_belyakov.repository.GeoapifyRepository
import com.example.weather_app_belyakov.repository.OpenWeatherRepository

class GeoapifyViewModel(private val repository: GeoapifyRepository) : ViewModel() {
    constructor() : this(GeoapifyRepository(GeoapifyApi().getClient().create(GeoapifyService::class.java)))

    fun loadAutocomplete(cityName: String) =
        repository.getAutocompleteCities(cityName)
}