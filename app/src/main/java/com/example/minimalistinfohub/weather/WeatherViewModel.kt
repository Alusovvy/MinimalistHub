package com.example.minimalistinfohub.weather

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherState = mutableStateOf(WeatherState())
    val weatherState: State<WeatherState> = _weatherState

    fun fetchWeather(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val response = weatherService.getAccurateWeather(lat, long)
                print(response)
                _weatherState.value = _weatherState.value.copy(
                    weather = Weather(response.latitude, response.longitude,response.current)
                )
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }


    data class WeatherState(
        val weather: Weather? = null
    )
}