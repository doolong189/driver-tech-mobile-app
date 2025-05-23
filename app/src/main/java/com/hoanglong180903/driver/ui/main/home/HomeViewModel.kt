package com.hoanglong180903.driver.ui.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.application.DriverApplication
import com.hoanglong180903.driver.data.responsemodel.ErrorResponse
import com.hoanglong180903.driver.data.requestmodel.GetNewOrderRequest
import com.hoanglong180903.driver.data.responsemodel.GetNewOrderResponse
import com.hoanglong180903.driver.data.requestmodel.GetStatisticalRequest
import com.hoanglong180903.driver.data.responsemodel.GetStatisticalResponse
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

    private val getNewOrderResult = MutableLiveData<Event<Resource<GetNewOrderResponse>>>()
    fun getNewOrderResult(): LiveData<Event<Resource<GetNewOrderResponse>>> {
        return getNewOrderResult
    }
    fun getNewOrder(request : GetNewOrderRequest) : Job = viewModelScope.launch {
        getNewOrderResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.getNewOrder(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        getNewOrderResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getNewOrderResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getNewOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getNewOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }


    fun getStatistical(request : GetStatisticalRequest) : Job = viewModelScope.launch {
        getStatisticalResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
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
                    getStatisticalResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getStatisticalResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }
}