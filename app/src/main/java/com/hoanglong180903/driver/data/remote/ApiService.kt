package com.hoanglong180903.driver.data.remote


import com.hoanglong180903.driver.data.enity.GetOrdersRequest
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/order/getOrdersForShipper")
    suspend fun getOrder(request : GetOrdersRequest) : Response<GetOrdersResponse>
}