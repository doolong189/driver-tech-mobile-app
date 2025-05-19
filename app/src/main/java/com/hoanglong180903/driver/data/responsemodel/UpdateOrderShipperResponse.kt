package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.Order

data class UpdateOrderShipperResponse (
    val message : String? = null,
    val updatedOrder : Order?
)