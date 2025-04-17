package com.hoanglong180903.driver.ui.dashboard.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.application.MyApplication
import com.hoanglong180903.driver.domain.enity.ErrorResponse
import com.hoanglong180903.driver.domain.enity.GetStatisticalRequest
import com.hoanglong180903.driver.domain.enity.GetStatisticalResponse
import com.hoanglong180903.driver.data.usecase.OrderRepository
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(private  val application: Application) : AndroidViewModel(application) {
    private val repository : OrderRepository = OrderRepository()

    private val getStatisticalResult = MutableLiveData<Event<Resource<GetStatisticalResponse>>>()

    fun getStatisticalResult() : LiveData<Event<Resource<GetStatisticalResponse>>>{
        return getStatisticalResult
    }

    fun getStatistical(request : GetStatisticalRequest) : Job = viewModelScope.launch {
        getStatisticalResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = repository.getStatistical(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        getStatisticalResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getStatisticalResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getStatisticalResult.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getStatisticalResult.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }
}