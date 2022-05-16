package com.reift.weatherapp.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("main")
	val main: Main? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@field:SerializedName("name")
	val cityName: String? = null

)

data class WeatherItem(

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("main")
	val main: String? = null,

	@field:SerializedName("id")
	val id: Int? = null

)

data class Main(

	@field:SerializedName("temp")
	val temp: Double? = null

)