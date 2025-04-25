package com.example.skylight
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skylight.ui.theme.SkylightTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkylightTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = { TopBanner( title = "Skylight") }
                    ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("home"){
                            HomeScreen(
                                modifier = Modifier.padding(innerPadding), onSeeForecastClick = { navController.navigate("forecast") }
                            )
                        }
                        composable("forecast") {
                            ForecastScreen()
                        }
                    }
                }
            }
        }
    }
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
@Composable
fun TopBanner(title: String, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(24.dp)

    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: WeatherViewModel = viewModel(), onSeeForecastClick: () -> Unit){
    val weather by viewModel.weatherData.collectAsState(initial = null)
    var zipCode by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        viewModel.fetchWeather("55379")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

        ){

        TextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text("Enter Zip Code") },
            placeholder = { Text("e.g. 10001") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth() .focusRequester(focusRequester),
            maxLines =1
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (zipCode.length == 5) {
                    viewModel.fetchWeather(zipCode)
                } else {
                    Toast.makeText(context, "Please enter a valid 5-digit zip code", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (weather != null) {
                weather?.let{Text(text = stringResource(R.string.loc, it.cityName), fontSize = 24.sp)}
            }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            weather?.let { weatherData ->
                val iconCode = weatherData.weather.firstOrNull()?.icon ?: "01d"
                val iconRes = getWeatherIcon(iconCode)
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(80.dp).weight(1f)
                )
                Text(text = stringResource(R.string.temp, weatherData.main.temp), fontSize = 64.sp)
            }
        }


        Row{
            Spacer(modifier = Modifier.width(12.dp))
            weather?.let {Text(text = stringResource(R.string.feels_like, it.main.feelsLike), modifier = Modifier.weight(1f))}
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row{
            weather?.let {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Low Temp: ${it.main.lowTemp}°F")
                    Text(text = "High Temp: ${it.main.highTemp}°F")
                    Text(text = "Pressure: ${it.main.pressure} hPa")
                    Text(text = "Humidity: ${it.main.humidity}%")
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSeeForecastClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.see_forecast))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SkylightTheme {
        HomeScreen(onSeeForecastClick = {})
    }
}