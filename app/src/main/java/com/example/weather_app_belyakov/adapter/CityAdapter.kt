package com.example.weather_app_belyakov.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app_belyakov.MainActivity
import com.example.weather_app_belyakov.databinding.CityViewholderBinding
import com.example.weather_app_belyakov.model.CityResponseApi

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    private lateinit var binding: CityViewholderBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CityViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        val binding = CityViewholderBinding.bind(holder.itemView)
        val ruName = differ.currentList[position]?.localNames?.ru
        val defaultName = differ.currentList[position].name
        val cityName = ruName ?: defaultName
        binding.cityTxt.text=cityName
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
            intent.putExtra("lat", differ.currentList[position].lat)
            intent.putExtra("lon", differ.currentList[position].lon)
            intent.putExtra("name", cityName)
            binding.root.context.startActivity(intent)
        }

    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback =
        object : DiffUtil.ItemCallback<CityResponseApi.CityResponseApiItem>() {
            override fun areItemsTheSame(
                oldItem: CityResponseApi.CityResponseApiItem,
                newItem: CityResponseApi.CityResponseApiItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CityResponseApi.CityResponseApiItem,
                newItem: CityResponseApi.CityResponseApiItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    var differ = AsyncListDiffer(this, differCallback)

}