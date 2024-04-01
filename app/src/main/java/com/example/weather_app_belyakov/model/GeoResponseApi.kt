package com.example.weather_app_belyakov.model

import com.google.gson.annotations.SerializedName

data class GeoResponseApi   (
    @SerializedName("name")
    val name: String,
    @SerializedName("local_names")
    val localNames: LocalNames,
    @SerializedName("lat")
    val lat: Number,
    @SerializedName("lon")
    val lon: Number,
    @SerializedName("country")
    val country: String,
    @SerializedName("state")
    val state: String

) {
    data class LocalNames(
        @SerializedName("en")
        val en: String,
        @SerializedName("ru")
        val ru: String,
    )
}
