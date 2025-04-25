package com.example.skylight.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("list") val daily: List<DailyForecast>
)

@Serializable
data class DailyForecast(
    val dt: Long,
    val temp: Temp,
    val humidity: Int,
    val weather: List<Weather>,
    @SerialName("wind_speed") val windSpeed: Double? = null
)

@Serializable
data class Temp(
    val day: Double,
    val min: Double,
    val max: Double
)

@Serializable
data class Weather(
    val description: String,
    val icon: String
)
