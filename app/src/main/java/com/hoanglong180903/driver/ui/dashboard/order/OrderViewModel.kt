package com.hoanglong180903.driver.ui.dashboard.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import com.hoanglong180903.driver.common.application.MyApplication
import com.hoanglong180903.driver.domain.enity.ErrorResponse
import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.domain.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.domain.enity.GetOrderShipIDResponse
import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import com.hoanglong180903.driver.domain.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.domain.enity.UpdateOrderShipperResponse
import com.hoanglong180903.driver.domain.usecase.GetDetailOrdersUseCase
import com.hoanglong180903.driver.domain.usecase.GetOrdersUseCase
import com.hoanglong180903.driver.utils.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class OrderViewModel(private val app: Application) : AndroidViewModel(app) {
    private val getOrdersUseCase by lazy { GetOrdersUseCase() }
    private val getDetailOrdersUseCase by lazy { GetDetailOrdersUseCase() }
    private val getOrderResult = MutableLiveData<Event<Resource<GetOrdersResponse>>>()
    fun getOrderResult(): LiveData<Event<Resource<GetOrdersResponse>>> {
        return getOrderResult
    }

    private val getDetailOrdersResult = MutableLiveData<Event<Resource<GetDetailOrderResponse>>>()
    fun getDetailOrdersResult() : LiveData<Event<Resource<GetDetailOrderResponse>>>{
        return getDetailOrdersResult
    }
    fun getOrders(request : GetOrdersRequest) : Job = viewModelScope.launch {
        getOrderResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = getOrdersUseCase(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        getOrderResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getOrderResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getOrderResult.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getOrderResult.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }

    fun getDetailOrders(request : GetDetailOrderRequest) : Job = viewModelScope.launch {
        getDetailOrdersResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())){
                val response = getDetailOrdersUseCase(request)
                if (response.isSuccessful){
                    response.body()?.let { result ->
                        getDetailOrdersResult.postValue(Event(Resource.Success(result)))
                    }
                }else{
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(),ErrorResponse::class.java)
                    }
                    getDetailOrdersResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        }catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getDetailOrdersResult.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getDetailOrdersResult.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }
}