package com.reift.weatherapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.reift.weatherapp.BuildConfig
import com.reift.weatherapp.data.ForecastResponse
import com.reift.weatherapp.data.WeatherResponse
import com.reift.weatherapp.databinding.ActivityMainBinding
import com.reift.weatherapp.helper.HelperFunction.formatterDegree
import com.reift.weatherapp.helper.HelperFunction.transparentStatusbar
import com.reift.weatherapp.helper.sizeIconWeather4x


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var mAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        transparentStatusbar(this)

        mAdapter = ForecastAdapter()

        searchCity()

        getWeatherByCurrentLoc()

        viewModel.getWeatherByCity().observe(this){
            setUpView(it, null)
        }

        viewModel.getForecastByCity().observe(this){
            setUpView(null, it)
        }
    }

    fun setUpView(weather: WeatherResponse?, forecast: ForecastResponse?){
        binding.apply {
            weather?.let {
                tvCity.text = it.cityName
                tvDegree.text = formatterDegree(it.main?.temp)

                val icon = it.weather?.get(0)?.icon
                val iconUrl = BuildConfig.ICON_URL + icon + sizeIconWeather4x
                Glide.with(applicationContext)
                    .load(iconUrl)
                    .into(imgIc)
            }

            forecast?.let {
                mAdapter.getForecastData(it.list)
                binding.rvWeather.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    adapter = mAdapter
                }
            }
        }
    }

    private fun getWeatherByCurrentLoc() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1000
            )
            return
        }
//        fusedLocationProviderClient.lastLocation
//            .addOnSuccessListener {
//                try {
//                    val lat = it.latitude
//                    val lon = it.longitude
//
//                    viewModel.weatherByCurrentLocation(lat, lon)
//                    viewModel.forecastByCurrentLocation(lat, lon)
//                } catch (e: Throwable){
//                    Log.e("failedFused", "Couldn't get latitude & longitude")
//                }
//            }
//            .addOnFailureListener {
//                Log.e("failedFused", "Failed getting current location")
//            }
        viewModel.weatherByCurrentLocation(0.283828, 241.2241)
        viewModel.forecastByCurrentLocation(21412421.2414241, 0.242414)
        viewModel.getForecastByCurrentLocation().observe(this){
            setUpView(null, it)
        }

        viewModel.getWeatherByCurrentLocation().observe(this){
            setUpView(it, null)
        }


    }

    private fun searchCity(){
        binding.svWeather.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.weatherByCity(it)
                        viewModel.forecastByCity(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            }
        )

    }
}