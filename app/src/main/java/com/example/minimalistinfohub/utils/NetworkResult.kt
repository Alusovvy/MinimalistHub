package com.example.minimalistinfohub.utils

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val code: Int, val data: T) : NetworkResult<T>()
    data class Error(val code: Int, val errorMsg: String?) : NetworkResult<Nothing>()
    data class Exception(val e: Throwable) : NetworkResult<Nothing>()
}