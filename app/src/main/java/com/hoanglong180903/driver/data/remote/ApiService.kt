package com.hoanglong180903.driver.data.remote


import com.hoanglong180903.driver.data.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.data.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/order/getOrdersForShipper")
    suspend fun getOrder(@Body request : GetOrdersRequest) : Response<GetOrdersResponse>

    @POST("order/getOrderDetail")
    suspend fun getDetailOrders(@Body request : GetDetailOrderRequest) : Response<GetDetailOrderResponse>
}