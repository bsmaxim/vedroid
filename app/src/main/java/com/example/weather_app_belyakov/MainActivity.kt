package com.example.weather_app_belyakov

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app_belyakov.adapter.ForecastAdapter
import com.example.weather_app_belyakov.databinding.ActivityMainBinding
import com.example.weather_app_belyakov.model.CurrentResponseApi
import com.example.weather_app_belyakov.model.ForecastResponseApi
import com.example.weather_app_belyakov.view_model.WeatherViewModel
import com.github.matteobattilana.weather.PrecipType
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()


        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = 55.164440
            var lon = 61.436844
            var name = "Chelyabinsk"

            // current temp
            cityTxt.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            statusTxt.text = it.weather?.get(0)?.main ?: "-"
                            windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + "Km"
                            humidityTxt.text = it.main?.humidity?.toString() + "%"
                            currentTempTxt.text =
                                it.main?.temp?.let { Math.round(it).toString() } + "°"
                            maxTempTxt.text =
                                it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                            minTempTxt.text =
                                it.main?.tempMin?.let { Math.round(it).toString() } + "°"

                            val drawable = if (isNightNow()) R.drawable.night_bg
                            else {
                                setDynamicallyWallpaper(it.weather?.get(0)?.icon ?: "-")
                            }
                            bgImage.setImageResource(drawable)
                            setEffectRainSnow(it.weather?.get(0)?.icon ?: "-")

                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })


            // settings Blur View
            var radius = 10f
            var decorView = window.decorView
            var rootView = (decorView.findViewById(android.R.id.content) as ViewGroup?)
            var windowsBackground = decorView.background

            rootView?.let {
                blurView.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowsBackground)
                    .setBlurRadius(radius)
                blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blurView.clipToOutline = true
            }


            // forecast temp
            weatherViewModel.loadForecastWeather(lat, lon, "metric")
                .enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                    override fun onResponse(
                        call: Call<ForecastResponseApi>,
                        response: Response<ForecastResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            blurView.visibility = View.VISIBLE

                            data?.let {
                                forecastAdapter.differ.submitList(it.list)
                                forecastView.apply {
                                    layoutManager=LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter=forecastAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }


    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 19
    }

    private fun setDynamicallyWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.rainy_bg
            }

            "13" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }

            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }

            else -> 0
        }
    }

    private fun setEffectRainSnow(icon: String) {
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "13" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "50" -> {
                initWeatherView(PrecipType.CLEAR)
            }
        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle = -20
            emissionRate = 100.0f
        }
    }
}