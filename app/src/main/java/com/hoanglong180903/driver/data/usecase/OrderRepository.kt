package com.hoanglong180903.driver.data.usecase

import com.hoanglong180903.driver.data.requestmodel.GetDetailOrderRequest
import com.hoanglong180903.driver.data.requestmodel.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.requestmodel.GetNewOrderRequest
import com.hoanglong180903.driver.data.requestmodel.GetStatisticalRequest
import com.hoanglong180903.driver.data.requestmodel.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.remote.RetrofitInstance

class OrderRepository {
    suspend fun getNewOrder(request : GetNewOrderRequest) = RetrofitInstance.api.getNewOrder(request)

    suspend fun getDetailOrder(request : GetDetailOrderRequest) = RetrofitInstance.api.getDetailOrders(request)

    suspend fun getStatistical(request : GetStatisticalRequest) = RetrofitInstance.api.getStatistical(request)

    suspend fun updateOrderShipper(request : UpdateOrderShipperRequest) = RetrofitInstance.api.updateOrderShipper(request)

    suspend fun getOrdersShipID(request : GetOrderShipIDRequest) = RetrofitInstance.api.getOrdersShipID(request)
}