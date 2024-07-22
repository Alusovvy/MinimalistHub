package com.example.minimalistinfohub.news

data class NewsData(
    val articles : List<News>
)

data class News(
    val source : NewsSource,
    val author : String,
    val title : String,
    val url : String,
    val urlToImage: String,
    val publishedAt: String,
    val description: String
)

data class NewsSource(
    val id : String,
    val name : String
)
