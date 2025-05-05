package com.hoanglong180903.driver.data.remote


import com.hoanglong180903.driver.data.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.data.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.data.enity.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.enity.GetOrderShipIDResponse
import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import com.hoanglong180903.driver.data.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.data.enity.GetShipperInfoResponse
import com.hoanglong180903.driver.data.enity.GetStatisticalRequest
import com.hoanglong180903.driver.data.enity.GetStatisticalResponse
import com.hoanglong180903.driver.data.enity.LoginAccountRequest
import com.hoanglong180903.driver.data.enity.LoginAccountResponse
import com.hoanglong180903.driver.data.enity.RegisterAccountRequest
import com.hoanglong180903.driver.data.enity.RegisterAccountResponse
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.enity.UpdateOrderShipperResponse
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

    @POST("")
    suspend fun getOrdersShipID(@Body request : GetOrderShipIDRequest) : Response<GetOrderShipIDResponse>

    @POST("shipper/register")
    suspend fun registerAccount(@Body request : RegisterAccountRequest) : Response<RegisterAccountResponse>

    @POST("shipper/login")
    suspend fun loginAccount(@Body request : LoginAccountRequest) : Response<LoginAccountResponse>
}