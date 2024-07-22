package com.example.minimalistinfohub.stock

import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("stock/metric?symbol=AAPL&metric=all&token=XD")
    suspend fun getStock() : StockData

    @GET("stock/metric")
    suspend fun getSpecificStock(
        @Query("symbol") symbol : String,
        @Query("metric") metric : String = "all",
        @Query("token") token : String = "XD"

    ) : StockData

    @GET("quote")
    suspend fun getStockPrice(
        @Query("symbol") symbol : String,
        @Query("token") token : String = "XD"
    ) : StockPrice
}