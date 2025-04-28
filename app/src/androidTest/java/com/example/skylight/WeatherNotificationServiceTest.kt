package com.example.skylight

import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherNotificationServiceTest {
    @Test
    fun testGetWeatherIcon_KnownIcons() {
        assertEquals(R.drawable.sunny64, getWeatherIcon("01d"))
        assertEquals(R.drawable.cloud, getWeatherIcon("02d"))
        assertEquals(R.drawable.heavyrain, getWeatherIcon("09n"))
        assertEquals(R.drawable.storm, getWeatherIcon("11d"))
        assertEquals(R.drawable.snow, getWeatherIcon("13n"))
        assertEquals(R.drawable.fog, getWeatherIcon("50d"))
    }
    @Test
    fun testGetWeatherIcon_UnknownIcon() {
        assertEquals(R.drawable.sunny64, getWeatherIcon("randomIcon"))
    }
}
