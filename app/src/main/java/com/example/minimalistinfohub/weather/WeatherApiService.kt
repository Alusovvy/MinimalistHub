package com.example.minimalistinfohub.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val apiCaller = Retrofit.Builder().baseUrl("https://api.open-meteo.com/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val weatherService: WeatherApiService = apiCaller.create(WeatherApiService::class.java)

interface WeatherApiService {
    @GET("forecast?latitude=51&longitude=17&current=temperature_2m")
    suspend fun getWeather(): Weather

    @GET("forecast")
    suspend fun getAccurateWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m"
    ): Weather
}