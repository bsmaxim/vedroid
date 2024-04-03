package com.example.weather_app_belyakov

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app_belyakov.adapter.CityAdapter
import com.example.weather_app_belyakov.databinding.ActivityCityListBinding
import com.example.weather_app_belyakov.model.CityResponseApi
import com.example.weather_app_belyakov.view_model.CityViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class CityListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCityListBinding
    private val cityAdapter by lazy { CityAdapter() }
    private val cityViewModel: CityViewModel by viewModels()

    private var autocompleteSearchJob: Job? = null
    private val debouncePeriod: Long = 150
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {

            cityEdit.addTextChangedListener(object : TextWatcher {
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
                    progressBar2.visibility = View.VISIBLE
                    autocompleteSearchJob?.cancel()
                    autocompleteSearchJob = lifecycleScope.launch {
                        s?.let {
                            delay(debouncePeriod)
                            cityViewModel.loadCity(s.toString(), 10)
                                .enqueue(object : retrofit2.Callback<CityResponseApi> {
                                    override fun onResponse(
                                        call: Call<CityResponseApi>,
                                        response: Response<CityResponseApi>
                                    ) {
                                        if (response.isSuccessful) {
                                            val data = response.body()
                                            data?.let {
                                                progressBar2.visibility = View.GONE
                                                cityAdapter.differ.submitList(it as List<CityResponseApi.CityResponseApiItem>?)
                                                cityView.apply {
                                                    layoutManager = LinearLayoutManager(
                                                        this@CityListActivity,
                                                        LinearLayoutManager.HORIZONTAL,
                                                        false
                                                    )
                                                    adapter = cityAdapter
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<CityResponseApi>,
                                        t: Throwable
                                    ) {

                                    }
                                })
                        }
                    }

                }
            })
        }
    }
}