package com.example.weather_app_belyakov

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = intent.getDoubleExtra("lat", 0.0)
            var lon = intent.getDoubleExtra("lon", 0.0)
            var name = intent.getStringExtra("name")


            if (lat == 0.0) {
                lat = 55.164440
                lon = 61.436844
                name = "Челябинск"
            }

            addCity.setOnClickListener {
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }

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
                            statusTxt.text = parseWeatherStatus(it.weather?.get(0))
                            windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + "Км"
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
            val radius = 10f
            val decorView = window.decorView
            val rootView = (decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowsBackground = decorView.background

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
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
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

    private fun parseWeatherStatus(weather: CurrentResponseApi.Weather?): String {
        if (weather === null) {
            return ""
        }

        val result = when (weather.id) {
            200 -> "Гроза с небольшим дождем"
            201 -> "Гроза с дождем"
            202 -> "Гроза с сильным дождем"
            210 -> "Легкая гроза"
            211 -> "Гроза"
            212 -> "Сильная гроза"
            221 -> "Неровная гроза"
            230 -> "Гроза с небольшим моросью"
            231 -> "Гроза с моросью"
            232 -> "Гроза с сильным моросящим дождем"
            300 -> "Слабый дождь"
            301 -> "Морось"
            302 -> "Сильный дождь изморось"
            310 -> "Небольшой дождь изморось"
            311 -> "Моросящий дождь"
            312 -> "Сильный моросящий дождь"
            313 -> "Дождь с моросью"
            314 -> "Сильный ливень и морось"
            321 -> "Дождь из душа"
            500 -> "Легкий дождь"
            501 -> "Умеренный дождь"
            502 -> "Сильный дождь"
            503 -> "Очень сильный дождь"
            504 -> "Сильный дождь"
            511 -> "Ледяной дождь"
            520 -> "Легкий дождь, дождь"
            521 -> "Ливень дождь"
            522 -> "Сильный ливень"
            531 -> "Неровный дождь"
            600 -> "Легкий снег"
            601 -> "Снег"
            602 -> "Сильный снегопад"
            611 -> "Мокрый снег"
            612 -> "Легкий дождь с мокрым снегом"
            613 -> "Мокрый дождь"
            615 -> "Небольшой дождь и снег"
            616 -> "Дождь и снег"
            620 -> "Легкий ливневый снег"
            621 -> "Shower snow"
            622 -> "Сильный снегопад"
            701 -> "Туман"
            711 -> "Курить"
            721 -> "Туман"
            731 -> "Песчано-пылевые вихри"
            741 -> "Туман"
            751 -> "Песок"
            761 -> "Пыль"
            762 -> "Вулканический пепел"
            771 -> "Шквалы"
            781 -> "Торнадо"
            800 -> "Чистое небо"
            801 -> "Мало облаков"
            802 -> "Рассеянные облака"
            803 -> "Разорванные облака"
            804 -> "Пасмурные облака"
            else -> ""
        }
        Log.i("ABOBA", "$weather $result")
        return result
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