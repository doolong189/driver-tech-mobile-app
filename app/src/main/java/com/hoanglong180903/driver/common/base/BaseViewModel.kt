package com.hoanglong180903.driver.common.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoanglong180903.driver.utils.DataResult
import com.hoanglong180903.driver.utils.NetworkHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseViewModel(val networkHelper: NetworkHelper)  : ViewModel(){
    protected val Loading : MutableLiveData<Boolean> = MutableLiveData(false)
    val _messageError = MutableLiveData<String>()
    val messageError : LiveData<String> = _messageError

    val isLoading : LiveData<Boolean> = Loading

    fun showLoading(){ return Loading.postValue(true) }
    fun hideLoading(){ return Loading.postValue(false) }

    enum class ErrorType {
        NETWORK, TIMEOUT, UNKNOWN
    }

    suspend inline fun <T> safeApiCall(
        result: MutableLiveData<DataResult<T>>,
        crossinline apiToCall: suspend () -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!networkHelper.isNetworkConnected()) {
                    result.postValue(DataResult.Error(""))
                    return@launch
                }
                apiToCall()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    Log.e("ApiCalls", "Call error: ${e.localizedMessage} code:$e", e.cause)
                    when (e) {
                        is HttpException -> {
                            val errorBody = e.response()?.errorBody()
                            val errorCode = e.response()?.code()
                            result.postValue(
                                DataResult.Error(
                                    error = e.message(),
                                    errorCode = errorCode ?: -1,
                                    errorBody = errorBody.toString()
                                )
                            )
                            Log.w("Call error :", "code:$errorCode")
                        }

                        is SocketTimeoutException -> result.postValue(DataResult.Error(ErrorType.TIMEOUT.name))
                        is IOException -> result.postValue(DataResult.Error(ErrorType.NETWORK.name))
                        else -> result.postValue(DataResult.Error(ErrorType.UNKNOWN.name))
                    }
                }
            }
        }
    }
}