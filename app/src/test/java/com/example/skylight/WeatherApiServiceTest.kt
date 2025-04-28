package com.example.skylight

import com.example.skylight.network.WeatherApi
import org.junit.Test
import org.junit.Assert
import kotlinx.coroutines.runBlocking

class WeatherApiServiceTest {

    private val weatherApiService = WeatherApi.service

    @Test
    fun testGetWeather_success() = runBlocking {
        val city = "Chicago"
        val apiKey = "OPENWEATHER_API_KEY"
        val weatherResponse = weatherApiService.getWeather(city, apiKey, "imperial")
        Assert.assertNotNull(weatherResponse)
        Assert.assertEquals(city, weatherResponse.cityName)
        Assert.assertTrue(weatherResponse.main.temp > -100)
    }

    @Test
    fun testGetForecast_success() = runBlocking {
        val city = "Chicago"
        val apiKey = "OPENWEATHER_API_KEY"
        val forecastResponse = weatherApiService.getForecast(city, apiKey, "imperial")
        Assert.assertNotNull(forecastResponse)
        Assert.assertTrue(forecastResponse.daily.isNotEmpty())
    }
}
