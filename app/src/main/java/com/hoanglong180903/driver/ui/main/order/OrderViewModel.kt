package com.hoanglong180903.driver.ui.main.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import com.hoanglong180903.driver.application.MyApplication
import com.hoanglong180903.driver.data.enity.ErrorResponse
import com.hoanglong180903.driver.data.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.data.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.repository.OrderRepository
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import com.hoanglong180903.driver.utils.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OrderViewModel(private val app: Application) : AndroidViewModel(app) {
    private val repository : OrderRepository = OrderRepository()

    private val getOrderResult = MutableLiveData<Event<Resource<GetOrdersResponse>>>()
    fun getOrderResult(): LiveData<Event<Resource<GetOrdersResponse>>> {
        return getOrderResult
    }


    fun getOrders(request : GetOrdersRequest) : Job = viewModelScope.launch {
        getOrderResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
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


}