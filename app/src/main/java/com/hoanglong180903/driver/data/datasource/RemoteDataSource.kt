package com.hoanglong180903.driver.data.datasource

import com.hoanglong180903.driver.data.api.ApiService
import com.hoanglong180903.driver.data.api.RetrofitInstance
import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import retrofit2.Response

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getOrders(request : GetOrdersRequest) : Response<GetOrdersResponse> {
        return apiService.getOrders(request)
    }

    suspend fun getDetailOrders(request : GetDetailOrderRequest) : Response<GetDetailOrderResponse>{
        return apiService.getDetailOrders(request)
    }
}