package com.hoanglong180903.driver.domain.usecase

import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import com.hoanglong180903.driver.domain.repository.OrderRepository
import retrofit2.Response

class GetOrdersUseCase() {
    private lateinit var orderRepository: OrderRepository
    suspend operator fun invoke(request : GetOrdersRequest) : Response<GetOrdersResponse> {
        return orderRepository.getOrders(request)
    }
}