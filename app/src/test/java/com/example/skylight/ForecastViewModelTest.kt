package com.example.skylight

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.skylight.network.DailyForecast
import com.example.skylight.network.ForecastViewModel
import org.junit.*

class ForecastViewModelTest {

    lateinit var forecastViewModel: ForecastViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        forecastViewModel = ForecastViewModel()
    }

    @Test
    fun testInitialState() {
        Assert.assertNull(forecastViewModel.forecastData.value)
        Assert.assertNull(forecastViewModel.errorMessage.value)
    }

    @Test
    fun testFetchForecast_success() {
        val city = "Chicago"
        val forecastObserver = Observer<List<DailyForecast>?> { forecastData ->
            if (forecastData != null) {
                Assert.assertTrue(forecastData.isNotEmpty())
                Assert.assertNull(forecastViewModel.errorMessage.value)
            }
        }
        forecastViewModel.forecastData.observeForever(forecastObserver)

        forecastViewModel.fetchForecast(city)
        Thread.sleep(5000)

        forecastViewModel.forecastData.removeObserver(forecastObserver)
    }

    @Test
    fun testFetchForecast_error() {
        val invalidCity = "NotARealCity"
        val errorObserver = Observer<String?> { errorMessage ->
            if (errorMessage != null) {
                Assert.assertTrue(errorMessage.isNotEmpty())
                Assert.assertNull(forecastViewModel.forecastData.value)
            }
        }
        forecastViewModel.errorMessage.observeForever(errorObserver)

        forecastViewModel.fetchForecast(invalidCity)
        Thread.sleep(5000)

        forecastViewModel.errorMessage.removeObserver(errorObserver)
    }

    @Test
    fun testErrorMessageDefaultFallback() {
        val invalidCity = "NotARealCity"
        forecastViewModel.fetchForecast(invalidCity)
        Thread.sleep(5000)
        val error = forecastViewModel.errorMessage.value
        Assert.assertTrue(error != null && error.isNotEmpty())
    }
}
