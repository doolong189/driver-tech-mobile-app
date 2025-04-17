package com.hoanglong180903.driver.domain.repository

import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import retrofit2.Response

interface OrderRepository {
    suspend fun getOrders(request : GetOrdersRequest) : Response<GetOrdersResponse>
    suspend fun getDetailOrders(request : GetDetailOrderRequest) : Response<GetDetailOrderResponse>
}