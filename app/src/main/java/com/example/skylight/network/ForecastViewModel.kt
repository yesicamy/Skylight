package com.example.skylight.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skylight.BuildConfig
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {

    private val _forecastData = MutableLiveData<List<DailyForecast>?>()
    val forecastData: LiveData<List<DailyForecast>?> = _forecastData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchForecast(city: String) {
        val apiKey = BuildConfig.OPENWEATHER_API_KEY
        viewModelScope.launch {
            try {
                val response = WeatherApi.service.getForecast(
                    city = city,
                    apiKey = apiKey,
                    units = "imperial"
                )
                _forecastData.value = response.daily
                _errorMessage.value = null
            } catch (e: Exception) {
                _forecastData.value = null
                _errorMessage.value = e.message ?: "An error occurred"
            }
        }
    }
}
