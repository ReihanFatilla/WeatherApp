package com.reift.weatherapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.reift.weatherapp.BuildConfig
import com.reift.weatherapp.R
import com.reift.weatherapp.data.ForecastResponse
import com.reift.weatherapp.data.WeatherResponse
import com.reift.weatherapp.databinding.ActivityMainBinding
import com.reift.weatherapp.helper.HelperFunction.formatterDegree
import com.reift.weatherapp.helper.HelperFunction.transparentStatusbar
import com.reift.weatherapp.helper.sizeIconWeather4x

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val mAdapter by lazy({ ForecastAdapter() })

    private var isLoading: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getWeatherByCity().observe(this) {
            setUpView(it, null)
        }

        viewModel.getForecastByCity().observe(this) {
            setUpView(null, it)
        }



        transparentStatusbar(this)

        searchCity()

        getWeatherByCurrentLoc()


    }

    private fun loadingStateView() {
        binding.apply {
            when (isLoading) {
                true -> {
                    layoutWeather.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
                false -> {
                    layoutWeather.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
                else -> {
                    layoutWeather.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
            }
        }

    }

    fun setUpView(weather: WeatherResponse?, forecast: ForecastResponse?) {
        binding.apply {
            weather?.let {
                tvCity.text = it.cityName
                tvDegree.text = formatterDegree(it.main?.temp)

                val iconId = it.weather?.get(0)?.icon
                val iconUrl = BuildConfig.ICON_URL + iconId + sizeIconWeather4x
                Glide.with(applicationContext)
                    .load(iconUrl)
                    .into(imgIc)

                val id = it.weather?.get(0)?.id
                setupBackroundImage(
                    id,
                    iconId
                )
            }

            forecast?.let {
                mAdapter.getForecastData(it.list)
                binding.rvWeather.apply {
                    layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    adapter = mAdapter
                }
            }
        }
    }

    private fun setupBackroundImage(idWeather: Int?, icon: String?) {
        idWeather?.let {
            when (idWeather) {
                in resources.getIntArray(R.array.thunderstorm_id_list) ->
                    setImageBackground(R.drawable.thunderstorm)
                in resources.getIntArray(R.array.drizzle_id_list) ->
                    setImageBackground(R.drawable.drizzle)
                in resources.getIntArray(R.array.rain_id_list) ->
                    setImageBackground(R.drawable.rain)
                in resources.getIntArray(R.array.freezing_rain_id_list) ->
                    setImageBackground(R.drawable.freezing_rain)
                in resources.getIntArray(R.array.snow_id_list) ->
                    setImageBackground(R.drawable.snow)
                in resources.getIntArray(R.array.sleet_id_list) ->
                    setImageBackground(R.drawable.sleet)

                in resources.getIntArray(R.array.clear_id_list) -> {
                    when (icon) {
                        "01d" -> setImageBackground(R.drawable.clear)
                        "01n" -> setImageBackground(R.drawable.clear_night)
                    }
                }

                in resources.getIntArray(R.array.clouds_id_list) ->
                    setImageBackground(R.drawable.lightcloud)
                in resources.getIntArray(R.array.heavy_clouds_id_list) ->
                    setImageBackground(R.drawable.heavycloud)
                in resources.getIntArray(R.array.fog_id_list) ->
                    setImageBackground(R.drawable.fog)
                in resources.getIntArray(R.array.sand_id_list) ->
                    setImageBackground(R.drawable.sand)
                in resources.getIntArray(R.array.dust_id_list) ->
                    setImageBackground(R.drawable.dust)
                in resources.getIntArray(R.array.volcanic_ash_id_list) ->
                    setImageBackground(R.drawable.volcanic)
                in resources.getIntArray(R.array.squalls_id_list) ->
                    setImageBackground(R.drawable.squalls)
                in resources.getIntArray(R.array.tornado_id_list) ->
                    setImageBackground(R.drawable.tornado)
            }
        }
    }

    private fun setImageBackground(imageBackround: Int) {
        Glide.with(this)
            .load(imageBackround)
            .into(findViewById(R.id.img_background))
    }

    private fun getWeatherByCurrentLoc() {
        isLoading = true
        loadingStateView()
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
        viewModel.weatherByCurrentLocation(0.0, 0.0)
        viewModel.forecastByCurrentLocation(1.9, 9.9)
        viewModel.getForecastByCurrentLocation().observe(this) {
            setUpView(null, it)
        }

        viewModel.getWeatherByCurrentLocation().observe(this) {
            setUpView(it, null)
            isLoading = false
            loadingStateView()
        }

    }

    private fun searchCity() {
        binding.svWeather.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        isLoading = true
                        loadingStateView()
                        try {
                            val inputMethodManager =
                                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
                        } catch (e: Throwable) {
                            Log.e("MainActivity", e.toString())
                        }
                        viewModel.weatherByCity(it)
                        viewModel.forecastByCity(it)
                        isLoading = false
                        loadingStateView()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            }
        )

    }
}