package com.hoanglong180903.driver.data.repository

import com.hoanglong180903.driver.data.datasource.RemoteDataSource
import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import com.hoanglong180903.driver.domain.repository.OrderRepository
import retrofit2.Response

class OrderRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : OrderRepository {
    override suspend fun getOrders(request: GetOrdersRequest): Response<GetOrdersResponse> {
        return remoteDataSource.getOrders(request)
    }

    override suspend fun getDetailOrders(request: GetDetailOrderRequest): Response<GetDetailOrderResponse> {
        return remoteDataSource.getDetailOrders(request)
    }

}