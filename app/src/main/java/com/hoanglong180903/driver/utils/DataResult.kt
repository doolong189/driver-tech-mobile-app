package com.hoanglong180903.driver.utils

sealed class DataResult<out R> {
    class Success<out T>(val data: T) : DataResult<T>()
    class Error<out T>(val error: String, val errorCode: Int = -1, val errorBody: String = "") :
        DataResult<T>()
    class SocketTimeoutException<out T> : DataResult<T>()
    class ErrorNetwork<out T>(val error: String) : DataResult<T>()
    data object Loading : DataResult<Nothing>()
}