package com.hoanglong180903.driver.api.remote


import com.hoanglong180903.driver.api.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.api.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.api.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.api.enity.GetOrderShipIDResponse
import com.hoanglong180903.driver.api.enity.GetOrdersRequest
import com.hoanglong180903.driver.api.enity.GetOrdersResponse
import com.hoanglong180903.driver.api.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.api.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.api.enity.GetStatisticalRequest
import com.hoanglong180903.driver.api.enity.GetStatisticalResponse
import com.hoanglong180903.driver.api.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.api.enity.UpdateOrderShipperResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/order/getOrdersForShipper")
    suspend fun getOrder(@Body request : GetOrdersRequest) : Response<GetOrdersResponse>

    @POST("order/getOrderDetail")
    suspend fun getDetailOrders(@Body request : GetDetailOrderRequest) : Response<GetDetailOrderResponse>

    @POST("shipper/statistical")
    suspend fun getStatistical(@Body request : GetStatisticalRequest) : Response<GetStatisticalResponse>

    @POST("order/updateOrderShipper")
    suspend fun updateOrderShipper(@Body request : UpdateOrderShipperRequest) : Response<UpdateOrderShipperResponse>

    @POST("shipper/getShipperInfo")
    suspend fun getShipperInfo(@Body request : GetShipperInfoRequest) : Response<GetShipperInfoResponse>

//    fun getUserInfo(): Call<GetUserInfoResponse>

    @POST("")
    suspend fun getOrdersShipID(@Body request : GetOrderShipIDRequest) : Response<GetOrderShipIDResponse>
}