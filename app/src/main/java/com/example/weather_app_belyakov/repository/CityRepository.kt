package com.example.weather_app_belyakov.repository

import com.example.weather_app_belyakov.network.ApiServices

class CityRepository(val api:ApiServices) {
    fun getCities(q:String,limit:Int)=
        api.getCitiesList(q,limit, "531a96ca0c18a0627ebab2a031ef5331")
}