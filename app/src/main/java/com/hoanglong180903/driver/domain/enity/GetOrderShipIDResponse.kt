package com.hoanglong180903.driver.domain.enity

import com.hoanglong180903.driver.domain.model.Order

data class GetOrderShipIDResponse (
    val message : String? = null,
    val data : List<Order>?
)