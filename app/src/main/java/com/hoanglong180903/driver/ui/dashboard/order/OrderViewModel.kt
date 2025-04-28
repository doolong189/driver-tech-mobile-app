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
import com.hoanglong180903.driver.common.application.DriverApplication
import com.hoanglong180903.driver.data.enity.ErrorResponse
import com.hoanglong180903.driver.data.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.enity.GetOrderShipIDResponse
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.usecase.OrderRepository
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperResponse
import com.hoanglong180903.driver.utils.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class OrderViewModel(private val app: DriverApplication) : AndroidViewModel(app) {
    private val repository : OrderRepository = OrderRepository()

    private val getOrderResult = MutableLiveData<Event<Resource<GetOrdersResponse>>>()
    fun getOrderResult(): LiveData<Event<Resource<GetOrdersResponse>>> {
        return getOrderResult
    }
    private val updateOrderShipperResult = MutableLiveData<Event<Resource<UpdateOrderShipperResponse>>>()

    fun updateOrderShipperResult() : LiveData<Event<Resource<UpdateOrderShipperResponse>>>{
        return updateOrderShipperResult
    }
    private val getOrdersShipIDResult = MutableLiveData<Event<Resource<GetOrderShipIDResponse>>>()

    fun getOrdersShipIDResult() : LiveData<Event<Resource<GetOrderShipIDResponse>>>{
        return getOrdersShipIDResult
    }

    fun getOrders(request : GetOrdersRequest) : Job = viewModelScope.launch {
        getOrderResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.getOrder(request)
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
                    getOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }

    fun updateOrderShipper(request : UpdateOrderShipperRequest) : Job = viewModelScope.launch {
        updateOrderShipperResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.updateOrderShipper(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        updateOrderShipperResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    updateOrderShipperResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    updateOrderShipperResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    updateOrderShipperResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }

    fun getOrdersShipID(request : GetOrderShipIDRequest) : Job = viewModelScope.launch {
        getOrdersShipIDResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.getOrdersShipID(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        getOrdersShipIDResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getOrdersShipIDResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getOrdersShipIDResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    ))))
                }
                else -> {
                    getOrdersShipIDResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.conversion_error
                    ))))
                }
            }
        }
    }
}