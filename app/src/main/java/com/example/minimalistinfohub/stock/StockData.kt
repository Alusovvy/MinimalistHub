package com.example.minimalistinfohub.stock

import com.google.gson.annotations.SerializedName

data class StockData(
    val metric: StockMetric,
    val metricType: String,
    val symbol: String
)

data class StockMetric(
    @SerializedName("10DayAverageTradingVolume")
    val tenDayAverageTradingVolume: String? = null,
    @SerializedName("26WeekPriceReturnDaily")
    val twentySixWeekPriceReturnDaily: String? = null,
    @SerializedName("52WeekHigh")
    val fiftyTwoWeeksHigh: String? = null,
    @SerializedName("52WeekHighDate")
    val fiftyTwoWeeksHighDate: String? = null,
    @SerializedName("52WeekLow")
    val fiftyTwoWeeksLow: String? = null,
    @SerializedName("52WeekLowDate")
    val fiftyTwoWeeksLowDate: String? = null
)

data class StockPrice(
    @SerializedName("c")
    val currentPrice: String? = null,
    @SerializedName("dp")
    val percentChange: String? = null,
)

data class StockPriceDAO(
    val symbol : String,
    val currentPrice: String,
    val percentChange: String
)
