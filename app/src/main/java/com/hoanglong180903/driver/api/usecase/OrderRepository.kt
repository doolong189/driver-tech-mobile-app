package com.hoanglong180903.driver.api.usecase

import com.hoanglong180903.driver.api.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.api.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.api.enity.GetOrdersRequest
import com.hoanglong180903.driver.api.enity.GetStatisticalRequest
import com.hoanglong180903.driver.api.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.api.remote.RetrofitInstance

class OrderRepository {
    suspend fun getOrder(request : GetOrdersRequest) = RetrofitInstance.api.getOrder(request)

    suspend fun getDetailOrder(request : GetDetailOrderRequest) = RetrofitInstance.api.getDetailOrders(request)

    suspend fun getStatistical(request : GetStatisticalRequest) = RetrofitInstance.api.getStatistical(request)

    suspend fun updateOrderShipper(request : UpdateOrderShipperRequest) = RetrofitInstance.api.updateOrderShipper(request)

    suspend fun getOrdersShipID(request : GetOrderShipIDRequest) = RetrofitInstance.api.getOrdersShipID(request)
}