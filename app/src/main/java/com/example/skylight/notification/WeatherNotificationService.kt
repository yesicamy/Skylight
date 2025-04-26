package com.example.skylight.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.skylight.BuildConfig
import com.example.skylight.MainActivity
import com.example.skylight.R
import com.example.skylight.network.WeatherApi
import com.example.skylight.network.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherNotificationService : Service() {

    companion object {
        const val CHANNEL_ID = "weather_channel"
        const val NOTIFICATION_ID = 1
    }

    private lateinit var notificationManager: NotificationManager

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather Notifications"
            val descriptionText = "Persistent weather notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showWeatherNotification(context: Context, weather: WeatherResponse) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val weatherIcon = getWeatherIcon(weather.weather.firstOrNull()?.icon ?: "01d")

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Weather: ${weather.cityName}")
            .setContentText("Temp: ${weather.main.temp}°F, ${weather.weather.firstOrNull()?.description}")
            .setSmallIcon(weatherIcon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Notification permission required", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel(this)

        CoroutineScope(Dispatchers.IO).launch {
            val weather = fetchWeather("New York")
            weather?.let {
                showWeatherNotification(this@WeatherNotificationService, it)
            }
        }

        return START_STICKY
    }
    private suspend fun fetchWeather(city: String): WeatherResponse? {
        return try {
            WeatherApi.service.getWeather(
                city = city,
                apiKey = BuildConfig.OPENWEATHER_API_KEY
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onBind(intent: Intent?) = null
}

fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.sunny64
        "01n" -> R.drawable.sunny64
        "02d" -> R.drawable.cloud
        "02n" -> R.drawable.cloud
        "03d", "03n" -> R.drawable.cloud
        "04d", "04n" -> R.drawable.cloud
        "09d", "09n" -> R.drawable.heavyrain
        "10d", "10n" -> R.drawable.heavyrain
        "11d", "11n" -> R.drawable.storm
        "13d", "13n" -> R.drawable.snow
        "50d", "50n" -> R.drawable.fog
        else -> R.drawable.sunny64
    }
}
