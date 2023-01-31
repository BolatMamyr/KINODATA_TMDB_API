package com.example.kinodata.utils

sealed class  NetworkResult <out T> {
    object Loading : NetworkResult<Nothing>()
    class Success <T>(val data: T) : NetworkResult<T>()
    class Error(val throwable: Throwable) : NetworkResult<Nothing>()
}