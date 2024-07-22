package com.example.minimalistinfohub.weather

data class Weather(val latitude: String,
                   val longitude: String,
                   val current: Current
    )

data class Current(val time: String, val interval: Int, val temperature_2m: Double)