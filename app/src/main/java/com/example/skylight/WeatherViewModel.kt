package com.example.skylight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skylight.network.WeatherApi
import com.example.skylight.network.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val _errorMessage = MutableStateFlow<String?>("No Errors")

    fun fetchWeather(city: String) {
        val apiKey = BuildConfig.OPENWEATHER_API_KEY
        viewModelScope.launch {
            try {
                val response = WeatherApi.service.getWeather(
                    city = city,
                    apiKey = apiKey,
                    units = "imperial"
                )
                println("Weather response: $response")
                _weatherData.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                println("API Error: ${e.message}")
                _weatherData.value = null
                _errorMessage.value = e.message ?: "An error occurred"
            }
        }
    }
}