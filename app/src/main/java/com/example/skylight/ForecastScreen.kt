package com.example.skylight

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.skylight.network.DailyForecast
import com.example.skylight.network.ForecastViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ForecastScreen(viewModel: ForecastViewModel = viewModel()) {
    val forecastData by viewModel.forecastData.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchForecast("55379")
    }
    errorMessage?.let {
        if (it.isNotEmpty()) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    forecastData?.let { forecastList ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(forecastList) { daily ->
                ForecastItem(forecast = daily)
            }
        }
    }
}

@Composable
fun ForecastItem(forecast: DailyForecast) {
    val date = formatDate(forecast.dt)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(text = "Date: $date")
        Text(text = "Temp: ${forecast.temp.day}°F")
        Text(text = "Low: ${forecast.temp.min}°F, High: ${forecast.temp.max}°F")
        Text(text = "Humidity: ${forecast.humidity}%")
        Text(text = "Wind Speed: ${forecast.windSpeed} mph")
        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp * 100)
    val formatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    return formatter.format(date)
}
