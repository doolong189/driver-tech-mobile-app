package com.hoanglong180903.driver.data.repository

import com.hoanglong180903.driver.data.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.enity.GetStatisticalRequest
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.remote.RetrofitInstance

class OrderRepository {
    suspend fun getOrder(request : GetOrdersRequest) = RetrofitInstance.api.getOrder(request)

    suspend fun getDetailOrder(request : GetDetailOrderRequest) = RetrofitInstance.api.getDetailOrders(request)

    suspend fun getStatistical(request : GetStatisticalRequest) = RetrofitInstance.api.getStatistical(request)

    suspend fun updateOrderShipper(request : UpdateOrderShipperRequest) = RetrofitInstance.api.updateOrderShipper(request)
}