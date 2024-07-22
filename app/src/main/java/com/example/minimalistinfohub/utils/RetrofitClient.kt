package com.example.minimalistinfohub.utils

import com.example.minimalistinfohub.location.GeoCodingAPIService
import com.example.minimalistinfohub.news.NewsApiService
import com.example.minimalistinfohub.stock.StockApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://maps.googleapis.com/"
    private const val NEWS_BASE_URL = "https://newsapi.org/v2/"
    private const val STOCK_BASE_URL = "https://finnhub.io/api/v1/"

    fun create(): GeoCodingAPIService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(GeoCodingAPIService::class.java)
    }

    fun createForNews(): NewsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(NewsApiService::class.java)
    }

    fun stockApiService(): StockApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(STOCK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(StockApiService::class.java)
    }
}