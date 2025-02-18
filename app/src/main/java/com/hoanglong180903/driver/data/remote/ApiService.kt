package com.hoanglong180903.driver.data.remote


import com.hoanglong180903.driver.domain.response.BillOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/order/orders")
    suspend fun requestGetBillOrder() : Response<BillOrderResponse>
}