package com.hoanglong180903.driver.data.remote


import com.hoanglong180903.driver.data.requestmodel.GetDetailOrderRequest
import com.hoanglong180903.driver.data.requestmodel.GetHistoryChatMessagesRequest
import com.hoanglong180903.driver.data.responsemodel.GetDetailOrderResponse
import com.hoanglong180903.driver.data.requestmodel.GetOrderShipIDRequest
import com.hoanglong180903.driver.data.responsemodel.GetOrderShipIDResponse
import com.hoanglong180903.driver.data.requestmodel.GetNewOrderRequest
import com.hoanglong180903.driver.data.responsemodel.GetNewOrderResponse
import com.hoanglong180903.driver.data.requestmodel.GetUserInfoRequest
import com.hoanglong180903.driver.data.responsemodel.GetUserInfoResponse
import com.hoanglong180903.driver.data.requestmodel.GetStatisticalRequest
import com.hoanglong180903.driver.data.responsemodel.GetStatisticalResponse
import com.hoanglong180903.driver.data.requestmodel.LoginAccountRequest
import com.hoanglong180903.driver.data.responsemodel.LoginAccountResponse
import com.hoanglong180903.driver.data.requestmodel.RegisterAccountRequest
import com.hoanglong180903.driver.data.responsemodel.RegisterAccountResponse
import com.hoanglong180903.driver.data.requestmodel.UpdateOrderShipperRequest
import com.hoanglong180903.driver.data.responsemodel.GetHistoryChatMessagesResponse
import com.hoanglong180903.driver.data.responsemodel.UpdateOrderShipperResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/order/getNewOrder")
    suspend fun getNewOrder(@Body request : GetNewOrderRequest) : Response<GetNewOrderResponse>

    @POST("order/getOrderDetail")
    suspend fun getDetailOrders(@Body request : GetDetailOrderRequest) : Response<GetDetailOrderResponse>

    @POST("shipper/statistical")
    suspend fun getStatistical(@Body request : GetStatisticalRequest) : Response<GetStatisticalResponse>

    @POST("order/updateOrderShipper")
    suspend fun updateOrderShipper(@Body request : UpdateOrderShipperRequest) : Response<UpdateOrderShipperResponse>

    @POST("user/getUserInfo")
    suspend fun getUserInfo(@Body request : GetUserInfoRequest) : Response<GetUserInfoResponse>

    @POST("/order/getOrdersForShipper")
    suspend fun getOrdersShipID(@Body request : GetOrderShipIDRequest) : Response<GetOrderShipIDResponse>

    @POST("user/register")
    suspend fun registerAccount(@Body request : RegisterAccountRequest) : Response<RegisterAccountResponse>

    @POST("user/login")
    suspend fun loginAccount(@Body request : LoginAccountRequest) : Response<LoginAccountResponse>

    @POST("chat-message/getHistoryChatMessages")
    suspend fun getHistoryChatMessages(@Body request : GetHistoryChatMessagesRequest) : Response<GetHistoryChatMessagesResponse>
}