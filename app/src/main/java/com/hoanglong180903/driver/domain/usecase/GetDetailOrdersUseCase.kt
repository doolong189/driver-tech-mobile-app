package com.hoanglong180903.driver.domain.usecase

import com.hoanglong180903.driver.domain.enity.GetDetailOrderRequest
import com.hoanglong180903.driver.domain.enity.GetDetailOrderResponse
import com.hoanglong180903.driver.domain.enity.GetOrdersRequest
import com.hoanglong180903.driver.domain.enity.GetOrdersResponse
import com.hoanglong180903.driver.domain.repository.OrderRepository
import retrofit2.Response

class GetDetailOrdersUseCase {
    private lateinit var orderRepository: OrderRepository
    suspend operator fun invoke(request : GetDetailOrderRequest) : Response<GetDetailOrderResponse> {
        return orderRepository.getDetailOrders(request)
    }
}