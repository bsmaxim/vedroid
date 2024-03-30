package com.example.weather_app_belyakov.repository

import com.example.weather_app_belyakov.network.GeoapifyService

class GeoapifyRepository(val api: GeoapifyService) {
    private val ApiKey = "6b3d15e2fc5248d19a3a75da45ae7898"

    fun getAutocompleteCities(cityName: String)=
        api.getAutocomplete(cityName, ApiKey)
}