package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.Order

data class GetOrderShipIDResponse (
    val message : String? = null,
    val data : List<Order>?
)