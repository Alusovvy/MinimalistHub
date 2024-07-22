package com.example.minimalistinfohub.news


import retrofit2.http.GET


interface NewsApiService {
    @GET("top-headlines?country=us&category=business&apiKey=heheNieDlaPsa")
    suspend fun getNews() : NewsData
}