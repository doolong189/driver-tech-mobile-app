package com.hoanglong180903.driver.data.api


import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.domain.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.domain.enity.GetOrderShipIDResponse
import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import com.hoanglong180903.driver.domain.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.domain.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.domain.enity.GetStatisticalRequest
import com.hoanglong180903.driver.domain.enity.GetStatisticalResponse
import com.hoanglong180903.driver.domain.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.domain.enity.UpdateOrderShipperResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/order/getOrdersForShipper")
    suspend fun getOrders(@Body request : GetOrdersRequest) : Response<GetOrdersResponse>

    @POST("order/getOrderDetail")
    suspend fun getDetailOrders(@Body request : GetDetailOrderRequest) : Response<GetDetailOrderResponse>

    @POST("shipper/statistical")
    suspend fun getStatistical(@Body request : GetStatisticalRequest) : Response<GetStatisticalResponse>

}