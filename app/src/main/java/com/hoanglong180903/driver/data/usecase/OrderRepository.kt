package com.hoanglong180903.driver.data.usecase

import com.hoanglong180903.driver.data.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.data.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.enity.GetNewOrderRequest
import com.hoanglong180903.driver.data.enity.GetStatisticalRequest
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.remote.RetrofitInstance

class OrderRepository {
    suspend fun getNewOrder(request : GetNewOrderRequest) = RetrofitInstance.api.getNewOrder(request)

    suspend fun getDetailOrder(request : GetDetailOrderRequest) = RetrofitInstance.api.getDetailOrders(request)

    suspend fun getStatistical(request : GetStatisticalRequest) = RetrofitInstance.api.getStatistical(request)

    suspend fun updateOrderShipper(request : UpdateOrderShipperRequest) = RetrofitInstance.api.updateOrderShipper(request)

    suspend fun getOrdersShipID(request : GetOrderShipIDRequest) = RetrofitInstance.api.getOrdersShipID(request)
}