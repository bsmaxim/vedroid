package com.example.weather_app_belyakov.model


import com.google.gson.annotations.SerializedName

data class GeoapifyAutocompleteResponse(
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("address_line1")
        val addressLine1: String,
        @SerializedName("address_line2")
        val addressLine2: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("country_code")
        val countryCode: String,
        @SerializedName("county")
        val county: String,
        @SerializedName("datasource")
        val datasource: Datasource,
        @SerializedName("formatted")
        val formatted: String,
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lon")
        val lon: Double,
        @SerializedName("place_id")
        val placeId: String,
        @SerializedName("plus_code")
        val plusCode: String,
        @SerializedName("plus_code_short")
        val plusCodeShort: String,
        @SerializedName("population")
        val population: Int,
        @SerializedName("postcode")
        val postcode: String,
        @SerializedName("rank")
        val rank: Rank,
        @SerializedName("region")
        val region: String,
        @SerializedName("result_type")
        val resultType: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("timezone")
        val timezone: Timezone
    ) {
        data class Datasource(
            @SerializedName("attribution")
            val attribution: String,
            @SerializedName("license")
            val license: String,
            @SerializedName("sourcename")
            val sourcename: String,
            @SerializedName("url")
            val url: String
        )

        data class Rank(
            @SerializedName("confidence")
            val confidence: Int,
            @SerializedName("confidence_city_level")
            val confidenceCityLevel: Int,
            @SerializedName("match_type")
            val matchType: String
        )

        data class Timezone(
            @SerializedName("name")
            val name: String,
            @SerializedName("offset_DST")
            val offsetDST: String,
            @SerializedName("offset_DST_seconds")
            val offsetDSTSeconds: Int,
            @SerializedName("offset_STD")
            val offsetSTD: String,
            @SerializedName("offset_STD_seconds")
            val offsetSTDSeconds: Int
        )
    }
}