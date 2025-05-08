package com.hoanglong180903.driver.ui.main.order.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.application.DriverApplication
import com.hoanglong180903.driver.data.enity.ErrorResponse
import com.hoanglong180903.driver.data.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.data.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.data.usecase.OrderRepository
import com.hoanglong180903.driver.utils.Event
import com.hoanglong180903.driver.utils.Resource
import com.hoanglong180903.driver.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class DetailOrderViewModel(private val application: Application) : AndroidViewModel(application) {

    private val repository : OrderRepository = OrderRepository()

    private val getDetailOrderResult = MutableLiveData<Event<Resource<GetDetailOrderResponse>>>()

    fun getDetailOrderResult(): LiveData<Event<Resource<GetDetailOrderResponse>>> {
        return getDetailOrderResult
    }
    fun getDetailOrder(request: GetDetailOrderRequest): Job = viewModelScope.launch {
        getDetailOrderResult.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<DriverApplication>())) {
                val response = repository.getDetailOrder(request)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        getDetailOrderResult.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    val errorResponse = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.string(), ErrorResponse::class.java)
                    }
                    getDetailOrderResult.postValue(Event(Resource.Error(errorResponse?.message ?: "")))
                }
            } else {
                getDetailOrderResult.postValue(
                    Event(
                        Resource.Error(getApplication<DriverApplication>().getString(
                            R.string.no_internet_connection)))
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    getDetailOrderResult.postValue(
                        Event(
                            Resource.Error(getApplication<DriverApplication>().getString(
                        R.string.network_failure
                    )))
                    )
                }
                else -> {
                    getDetailOrderResult.postValue(
                        Event(
                            Resource.Error(getApplication<DriverApplication>().getString(
                                R.string.conversion_error)))
                    )
                }
            }
        }
    }

}