package com.example.skylight.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

@Serializable
data class WeatherResponse(
    @SerialName("name") val cityName: String,
    @SerialName("main") val main: Main,
    @SerialName("weather") val weather: List<WeatherDescription>
)

@Serializable
data class WeatherDescription(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)


@Serializable
data class Main(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike : Double,
    @SerialName("temp_min") val lowTemp : Double,
    @SerialName("temp_max") val highTemp : Double,
    val pressure : Int,
    val humidity : Int
)

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): WeatherResponse

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial",
        @Query("cnt") count: Int = 16 // Fetch 16 days forecast
    ): ForecastResponse
}
object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val contentType = "application/json".toMediaType()

    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val service: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}
