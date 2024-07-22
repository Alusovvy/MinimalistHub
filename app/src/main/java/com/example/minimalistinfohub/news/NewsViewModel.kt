package com.example.minimalistinfohub.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimalistinfohub.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _news = MutableStateFlow<NewsData?>(null)
    val news: StateFlow<NewsData?> = _news


    init {
        fetchNews()
    }

    fun fetchNews() {
        try {
            viewModelScope.launch {
                val result = RetrofitClient.createForNews().getNews()

                _news.value = result
            }

        }catch (e:Exception) {
            Log.d("res1", "${e.cause} " + " ${e.message}")
        }
    }
}