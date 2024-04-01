package com.example.weather_app_belyakov.view_model

import androidx.lifecycle.ViewModel
import com.example.weather_app_belyakov.network.ApiClient
import com.example.weather_app_belyakov.network.ApiServices
import com.example.weather_app_belyakov.repository.CityRepository

class CityViewModel(val repository: CityRepository): ViewModel() {
    constructor():this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q:String,limit:Int)=
        repository.getCities(q,limit)
}