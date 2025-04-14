package com.hoanglong180903.driver.api.enity

import com.hoanglong180903.driver.model.Order

data class GetOrderShipIDResponse (
    val message : String? = null,
    val data : List<Order>?
)