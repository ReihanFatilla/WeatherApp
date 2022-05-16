package com.reift.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reift.weatherapp.data.WeatherResponse
import com.reift.weatherapp.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val weatherByCity = MutableLiveData<WeatherResponse>()

    fun weatherByCity(city: String){
        ApiConfig.getApiService().weatherByCity(city)
            .enqueue(object : Callback<WeatherResponse>{
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) weatherByCity.postValue(response.body())
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.i("FailResponse", t.message.toString())
                }

            })
    }

    fun getWeatherByCity(): LiveData<WeatherResponse> = weatherByCity

}