package com.reift.weatherapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reift.weatherapp.BuildConfig
import com.reift.weatherapp.data.ListItem
import com.reift.weatherapp.databinding.ItemWeatherBinding
import com.reift.weatherapp.helper.HelperFunction.formatterDegree
import com.reift.weatherapp.helper.sizeIconWeather2x
import com.reift.weatherapp.helper.sizeIconWeather4x
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.MyViewHolder>() {

    private val listForecast = ArrayList<ListItem>()

    class MyViewHolder (val binding: ItemWeatherBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MyViewHolder (
        ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val forecast = listForecast[position]
        holder.binding.apply {
            tvMaxDegree.text = "Max: " + formatterDegree(forecast.main?.tempMax)
            tvMinDegree.text = "Min: " + formatterDegree(forecast.main?.tempMin)

            val date = forecast.dtTxt?.take(10)
            val time = forecast.dtTxt?.takeLast(8)

            val dateArray = date?.split("-")?.toTypedArray()
            val timeArray = time?.split(":")?.toTypedArray()

            val calendar = Calendar.getInstance()

            // Date
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray?.get(0) as String))
            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]  ) - 1)
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))

            // Time
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray?.get(0) as String))
            calendar.set(Calendar.MINUTE, 0)

            val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
                .format(calendar.time).toString()

            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                .format(calendar.time).toString()

            tvDate.text = dateFormat
            tvTime.text = timeFormat

            val icon = forecast.weather?.get(0)?.icon
            val iconUrl = BuildConfig.ICON_URL + icon + sizeIconWeather2x
            Glide.with(imgItemWeather.context)
                .load(iconUrl)
                .into(imgItemWeather)
        }
    }

    override fun getItemCount() = listForecast.size

    fun getForecastData(data: List<ListItem>?){
        if(data == null) return
        listForecast.clear()
        listForecast.addAll(data)
    }
}