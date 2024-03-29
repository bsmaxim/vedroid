package com.example.weather_app_belyakov

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app_belyakov.databinding.ActivityMainBinding
import com.example.weather_app_belyakov.model.OpenWeatherCurrentResponse
import com.example.weather_app_belyakov.model.OpenWeatherGeoResponse
import com.example.weather_app_belyakov.view_model.OpenWeatherViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel = OpenWeatherViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            var lat=50.50
            var lon=0.12
            var name="London"

            autoCompleteTextView.setText(name)
            testLanLon.setText("ABOBA")

            weatherViewModel.loadGeoInfo(name).enqueue(object: retrofit2.Callback<OpenWeatherGeoResponse> {
                override fun onResponse(
                    call: Call<OpenWeatherGeoResponse>,
                    response: Response<OpenWeatherGeoResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            testLanLon.setText("lon=${data.lon} lat=${data.lat}")
                        }
                    }
                }

                override fun onFailure(p0: Call<OpenWeatherGeoResponse>, p1: Throwable) {

                }
            })

            weatherViewModel.loadCurrentWeatherInfo(lat, lon, "metric").enqueue(object: retrofit2.Callback<OpenWeatherCurrentResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<OpenWeatherCurrentResponse>,
                    response: Response<OpenWeatherCurrentResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            testLanLon.setText("${data.base} ${data.id}")
                        }
                    }
                }

                override fun onFailure(call: Call<OpenWeatherCurrentResponse>, thrw: Throwable) {
//                    TODO("Not yet implemented")
                    Toast.makeText(getApplicationContext(), thrw.message, Toast.LENGTH_LONG).show();
                    // aboba
                }

            })

        }

    }
}