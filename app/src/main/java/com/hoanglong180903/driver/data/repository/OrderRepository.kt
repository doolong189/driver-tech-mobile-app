package com.hoanglong180903.driver.data.repository

import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.remote.RetrofitInstance

class OrderRepository {
    suspend fun getOrder(request : GetOrdersRequest) = RetrofitInstance.api.getOrder(request)
}