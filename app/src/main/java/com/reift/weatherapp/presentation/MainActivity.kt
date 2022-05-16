package com.reift.weatherapp.presentation

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.reift.weatherapp.databinding.ActivityMainBinding
import com.reift.weatherapp.helper.HelperFunction.transparentStatusbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        transparentStatusbar(this)

        searchCity()

        viewModel.getWeatherByCity().observe(this){
            binding.apply {
                tvCity.text = it.cityName
                tvDegree.text = it.main?.temp.toString()
            }
        }

    }

    private fun searchCity(){
        binding.svWeather.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    try {
//                        val i
//                    }
                    query?.let {
                        viewModel.weatherByCity(it)
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