package com.example.weather_app_belyakov.network

import com.example.weather_app_belyakov.model.GeoapifyAutocompleteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoapifyService {
    @GET("v1/geocode/autocomplete")
    fun getAutocomplete(
        @Query("text") cityName: String,
        @Query("apikey") apiKey: String,
    ): Call<List<GeoapifyAutocompleteResponse>>
}