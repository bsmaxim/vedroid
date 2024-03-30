package com.example.weather_app_belyakov

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weather_app_belyakov.databinding.ActivityMainBinding
import com.example.weather_app_belyakov.model.OpenWeatherCurrentResponse
import com.example.weather_app_belyakov.model.OpenWeatherGeoResponse
import com.example.weather_app_belyakov.view_model.OpenWeatherViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel = OpenWeatherViewModel()
    private val citiesList = mutableListOf<String>()
    private val citiesAdapter =
        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, citiesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            var lat = 50.50
            var lon = 0.12
            var name = "London"

            autoCompleteTextView.setAdapter(citiesAdapter)
            autoCompleteTextView.setText(name)

            weatherViewModel.loadGeoInfo(name)
                .enqueue(object : retrofit2.Callback<OpenWeatherGeoResponse> {
                    override fun onResponse(
                        call: Call<OpenWeatherGeoResponse>,
                        response: Response<OpenWeatherGeoResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let {
                                lat = data.lat as Double
                                lon = data.lon as Double
                            }
                        }
                    }

                    override fun onFailure(call: Call<OpenWeatherGeoResponse>, thrw: Throwable) {
                        Toast.makeText(applicationContext, thrw.message, Toast.LENGTH_SHORT).show();
                    }
                })

            weatherViewModel.loadCurrentWeatherInfo(lat, lon, "metric")
                .enqueue(object : retrofit2.Callback<OpenWeatherCurrentResponse> {
                    override fun onResponse(
                        call: Call<OpenWeatherCurrentResponse>,
                        response: Response<OpenWeatherCurrentResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let {
                                testLanLon.setText("${data.name} ${data.coord.toString()}")
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<OpenWeatherCurrentResponse>,
                        thrw: Throwable
                    ) {
                        Toast.makeText(applicationContext, thrw.message, Toast.LENGTH_SHORT).show();
                    }

                })
        }

        setupAutocompleteListeners()

    }

    private var autocompleteSearchJob: Job? = null
    private val debouncePeriod: Long = 100
    private fun setupAutocompleteListeners() {
        binding.apply {
            autoCompleteTextView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    autocompleteSearchJob?.cancel()
                    autocompleteSearchJob = lifecycleScope.launch {
                        s?.let {
                            delay(debouncePeriod)
                            requestAutocomplete(it.toString())
                        }

                    }
                }
            })
        }
    }

    private fun requestAutocomplete(city_name: String) {
        binding.apply {
            // TODO(сделать запрос к апи автодополнение)
        }
    }
}