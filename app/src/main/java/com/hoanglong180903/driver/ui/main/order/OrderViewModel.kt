package com.hoanglong180903.driver.ui.main.order

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
import com.hoanglong180903.driver.data.usecase.OrderRepository
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperResponse
import com.hoanglong180903.driver.utils.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class OrderViewModel(private val app: Application) : AndroidViewModel(app) {
    private val repository : OrderRepository = OrderRepository()
    private val updateOrderShipperResult = MutableLiveData<Event<Resource<UpdateOrderShipperResponse>>>()
    private val getOnGoingOrderResult = MutableLiveData<Event<Resource<GetOrderShipIDResponse>>>()
    private val getCompletedOrderResult = MutableLiveData<Event<Resource<GetOrderShipIDResponse>>>()
    private val getCancelOrderResult = MutableLiveData<Event<Resource<GetOrderShipIDResponse>>>()

    fun updateOrderShipperResult() : LiveData<Event<Resource<UpdateOrderShipperResponse>>>{
        return updateOrderShipperResult
    }

    fun getOnGoingOrderResult(): LiveData<Event<Resource<GetOrderShipIDResponse>>> {
        return getOnGoingOrderResult
    }
    fun getCompletedOrderResult() : LiveData<Event<Resource<GetOrderShipIDResponse>>>{
        return getCompletedOrderResult
    }
    fun getCancelOrderResult() : LiveData<Event<Resource<GetOrderShipIDResponse>>>{
        return getCancelOrderResult
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
        if (request.receiptStatus == 1) {
            getOnGoingOrderResult.postValue(Event(Resource.Loading()))
            try {
                if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                    val response = repository.getOrdersShipID(request)
                    if (response.isSuccessful) {
                        response.body()?.let { resultResponse ->
                            getOnGoingOrderResult.postValue(Event(Resource.Success(resultResponse)))
                        }
                    } else {
                        val errorResponse = response.errorBody()?.let {
                            val gson = Gson()
                            gson.fromJson(it.string(), ErrorResponse::class.java)
                        }
                        getOnGoingOrderResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                    }
                } else {
                    getOnGoingOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.no_internet_connection
                    ))))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> {
                        getOnGoingOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                            R.string.network_failure
                        ))))
                    }

                    else -> {
                        getOnGoingOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                            R.string.conversion_error
                        ))))
                    }
                }
            }
        }else if (request.receiptStatus == 2){
            getCompletedOrderResult.postValue(Event(Resource.Loading()))
            try {
                if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                    val response = repository.getOrdersShipID(request)
                    if (response.isSuccessful) {
                        response.body()?.let { resultResponse ->
                            getCompletedOrderResult.postValue(Event(Resource.Success(resultResponse)))
                        }
                    } else {
                        val errorResponse = response.errorBody()?.let {
                            val gson = Gson()
                            gson.fromJson(it.string(), ErrorResponse::class.java)
                        }
                        getCompletedOrderResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                    }
                } else {
                    getCompletedOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.no_internet_connection
                    ))))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> {
                        getCompletedOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                            R.string.network_failure
                        ))))
                    }

                    else -> {
                        getCompletedOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                            R.string.conversion_error
                        ))))
                    }
                }
            }
        }else{
            getCancelOrderResult.postValue(Event(Resource.Loading()))
            try {
                if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                    val response = repository.getOrdersShipID(request)
                    if (response.isSuccessful) {
                        response.body()?.let { resultResponse ->
                            getCancelOrderResult.postValue(Event(Resource.Success(resultResponse)))
                        }
                    } else {
                        val errorResponse = response.errorBody()?.let {
                            val gson = Gson()
                            gson.fromJson(it.string(), ErrorResponse::class.java)
                        }
                        getCancelOrderResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                    }
                } else {
                    getCancelOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(R.string.no_internet_connection))))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> {
                        getCancelOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(
                            R.string.network_failure
                        ))))
                    }

                    else -> {
                        getCancelOrderResult.postValue(Event(Resource.Error(getApplication<DriverApplication>().getString(R.string.conversion_error))))
                    }
                }
            }
        }
    }
}