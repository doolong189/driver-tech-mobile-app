package com.hoanglong180903.driver.presentation.ui.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.Utils.Resource
import com.hoanglong180903.driver.Utils.Utils
import com.hoanglong180903.driver.application.MyApplication
import com.hoanglong180903.driver.data.repository.OrderRepository
import com.hoanglong180903.driver.domain.response.BillOrderResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OrderViewModel(
    app: Application,
) : AndroidViewModel(app) {
    private val orderRepository = OrderRepository()
    val ordersData: MutableLiveData<Resource<BillOrderResponse>> = MutableLiveData()
    init {
        getOrders()
    }

    fun getOrders() = viewModelScope.launch {
        fetchOrders()
    }

    private suspend fun fetchOrders() {
        ordersData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = orderRepository.getOrder()
                ordersData.postValue(handlePicsResponse(response))
            } else {
                ordersData.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException ->{
                    ordersData.postValue(
                        Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.network_failure
                            )
                        )
                    )
                    Log.e("zzz o day",t.localizedMessage)
                }

                else ->{
                    ordersData.postValue(
                        Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.conversion_error
                            )
                        )
                    )
                    Log.e("zzz o day",t.localizedMessage)
                }
            }
        }
    }

    private fun handlePicsResponse(response: Response<BillOrderResponse>): Resource<BillOrderResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    class OrdersViewModelFactory(private val application : Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(application) as T
            }else{
                throw IllegalArgumentException("viewmodel not found")
            }

        }
    }
}